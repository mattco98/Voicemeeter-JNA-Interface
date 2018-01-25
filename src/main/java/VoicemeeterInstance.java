/*
 * COPYRIGHT: Vincent Burel (c) 2015 All Rights Reserved
 */

import com.sun.jna.*;
import com.sun.jna.win32.StdCallLibrary;

import java.util.Arrays;
import java.util.List;

/**
 * JNA interface modelling the VoicemeeterRemote.h file found in the
 * Voicemeeter Remote API Pack, version 6. All comments been have taken from the
 * VoicemeeterRemote.h header file.
 */
@SuppressWarnings("unused")
public interface VoicemeeterInstance extends StdCallLibrary {

    //******************************************************************************//
    //*                                    Login                                   *//
    //******************************************************************************//

    /**
     * Opens the communication pipe with Voicemeeter. Must be called on software
     * startup.
     *
     * @return Status code
     *              0:  OK (no error)
     *              1:  OK, however Voicemeeter is not open
     *              -1: Cannot get the client
     *              -2: Unexpected login (The client was already logged in)
     */
    int VBVMR_Login();

    /**
     * Closes the communication pipe with Voicemeeter. Must be called on software
     * shutdown.
     *
     * @return Status code
     *              0: OK (no error)
     */
    int VBVMR_Logout();

    /**
     * Run the Voicemeeter application. Gets the directory and runs the program.
     *
     * @param type Voicemeeter type.
     *                  1: Voicemeeter
     *                  2: Voicemeeter Banana
     * @return Status code
     *              0:  OK (no error)
     *              -1: Voicemeeter not installed
     */
    int VBVMR_RunVoicemeeter(int type);


    //******************************************************************************//
    //*                             General Information                            *//
    //******************************************************************************//

    /**
     * Gets Voicemeeter type.
     *
     * @param type 32bit long pointer that will receive the type.
     *                  1: Voicemeeter
     *                  2: Voicemeeter Banana
     * @return Status code
     *              0:  OK (no error)
     *              -1: Cannot get client
     *              -2: No server
     */
    int VBVMR_GetVoicemeeterType(Pointer type);

    /**
     * Gets Voicemeeter version.
     * @param version 32bit int pointer that will receive the version
     *                v1 = (version & 0xFF000000) >> 24;
     *                v2 = (version & 0x00FF0000) >> 16;
     *                v3 = (version & 0x0000FF00) >> 8;
     *                v4 = (version & 0x000000FF);
     * @return Status code
     *              0:  OK (no error)
     *              -1: Cannot get client
     *              -2: No server
     */
    int VBVMR_GetVoicemeeterVersion(Pointer version);


    //******************************************************************************//
    //*                               Get parameters                               *//
    //******************************************************************************//

    /**
     * Check if parameters have changed. Useful for any GUI displays that display
     * parameter values. Not thread safe.
     *
     * @return 0:  No parameters have changed
     *          1:  Parameters have been changed
     *          -1: Error
     *          -2: No server
     */
    int VBVMR_IsParametersDirty();

    /**
     * Get float parameter value. See parameter table values for examples.
     *
     * @param paramName ASCII string pointer containing the name of the parameter
     * @param value 32bit float pointer that will receive the value of the
     *               parameter.
     * @return Status code
     *              0:  OK (no error)
     *              -1: Error
     *              -2: No server
     *              -3: Unknown parameter
     *              -5: Structure mismatch
     */
    int VBVMR_GetParameterFloat(Pointer paramName, Pointer value);

    /**
     * Get string paramter value
     *
     * @param paramName ASCII string pointer containing the name of the parameter
     * @param string 64bit string pointer that will receive the value of the
     *                parameter.
     * @return Status code
     *              0:  OK (no error)
     *              -1: Error
     *              -2: No server
     *              -3: Unknown Parameter
     *              -5: Structure mismatch
     */
    int VBVMR_GetParameterStringA(Pointer paramName, Pointer string);
    int VBVMR_GetParameterStringW(Pointer paramName, Pointer string);


    //******************************************************************************//
    //*                                Get levels                                  *//
    //******************************************************************************//

    /**
     * Get level data. Not thread safe.
     *
     * Voicemeeter Channel Assignment:
     *
     *      | Strip 1 | Strip 2|              Virtual Input             |
     *      |---------|--------|----------------------------------------|
     *      | 00 | 01 | 02 | 03 | 04 | 05 | 06 | 07 | 08 | 09 | 10 | 11 |
     *
     *      |             Output A1 / A2            |             Virtual Output            |
     *      +----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+
     *      | 00 | 01 | 02 | 03 | 04 | 05 | 06 | 07 | 08 | 09 | 10 | 11 | 12 | 13 | 14 | 15 |
     *
     * Voicemeeter Banana Channel Assignment:
     *
     *       | Strip 1 | Strip 2 | Strip 3 |             Virtual Input             |            Virtual Input AUX          |
     *       +----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+
     *       | 00 | 01 | 02 | 03 | 04 | 05 | 06 | 07 | 08 | 09 | 10 | 11 | 12 | 13 | 14 | 15 | 16 | 17 | 18 | 19 | 20 | 21 |
     *
     *       |             Output A1                 |                Output A2              |                Output A3              |
     *       +----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+
     *       | 00 | 01 | 02 | 03 | 04 | 05 | 06 | 07 | 08 | 09 | 10 | 11 | 12 | 13 | 14 | 15 | 16 | 17 | 18 | 19 | 20 | 21 | 22 | 23 |
     *
     *       |            Virtual Output B1          |             Virtual Output B2         |
     *       +----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+
     *       | 24 | 25 | 26 | 27 | 28 | 29 | 30 | 31 | 32 | 33 | 34 | 35 | 36 | 37 | 38 | 39 |
     *
     *
     *
     * @param type The type of level to get.
     *                  0: Pre-fader input levels
     *                  1: Post-fader input levels
     *                  2: Post-mute input levels
     *                  3: Output levels
     * @param channel Zero-indexed audio channel.
     * @param value 32bit float pointer that will receive the level value.
     *
     * @return Status code
     *              0:  OK (no error)
     *              -1: Error
     *              -2: No server
     *              -3: No level available
     *              -4: type of channel out of allowed range
     */
    int VBVMR_GetLevel(int type, int channel, Pointer value);

    /**
     * Gets MIDI message from MIDI input devices used by Voicemeeter MIDI mapping.
     * Not thread safe.
     *
     * @param midiBuffer Pointer for MIDI buffer. Expected messages size is below
     *                    4 bytes, however it is recommend to have at least 1024
     *                    bytes to receive possibly multiple MIDI event messages
     *                    at once.
     * @param byteMax Size of the midiBuffer pointer.
     * @return Status Code
     *              >0:       Numer of bytes placed in buffer
     *              -1:       Error
     *              -2:       No server
     *              -5 OR -6: No MIDI data
     */
    int VBVMR_GetMidiMessage(Pointer midiBuffer, int byteMax);


    //******************************************************************************//
    //*                               Set Parameters                               *//
    //******************************************************************************//

    /**
     * Set a 32bit float parameter.
     *
     * Example parameter names:
     *      Strip[1].gain
     *      Strip[0].mute
     *      Bus[0].gain
     *      Bus[0].eq.channel[0].cell[0].gain
     *
     * @param paramName ASCII string pointer containing the name of the parameter
     * @param value 32bit float pointer that will receive the value of the
     *               parameter.
     * @return Status Code:
     *              0:  OK (no error)
     *              -1: Error
     *              -2: No server
     *              -3: Unknown Parameter.
     */
    int VBVMR_SetParameterFloat(Pointer paramName, float value);

    /**
     * Set a string parameter.
     *
     * @param paramName ASCII string pointer containing the name of the parameter
     * @param string ASCII string pointer containing the new value of the
     *                parameter
     * @return Status code
     *              0:  OK (no error)
     *              -1: Error
     *              -2: No server
     *              -3: Unknown Parameter
     */
    int VBVMR_SetParameterStringA(Pointer paramName, Pointer string);
    int VBVMR_SetParameterStringW(Pointer paramName, Pointer string);

    /**
     * Set one or several parameter using a script less the 48 kB. Separate
     * different assignments with ',', ';', or '\n'. See the scripts folder in the
     * Voicemeeter Remote API pack for examples.
     *
     * @param paramScript ASCII string pointer containing the script.
     * @return Status code
     *              0:  OK (no error)
     *              >0: Error, where the number is the line number that caused
     *                  the error
     *              -1: Error
     *              -2: No server
     *              -3 OR -4: Unexpected error
     */
    int VBVMR_SetParameters(Pointer paramScript);
    int VBVMR_SetParametersW(Pointer paramScript);


    //******************************************************************************//
    //*                            DEVICES ENUMERATOR                              *//
    //******************************************************************************//

    int VBVMR_RESULT_OK = 0;
    int VBVMR_DEVTYPE_MME = 1;
    int VBVMR_DEVTYPE_WDM = 3;
    int VBVMR_DEVTYPE_KS = 4;
    int VBVMR_DEVTYPE_ASIO = 5;

    /**
     * Gets the number of audio output devices available on the system.
     *
     * @return Number of devices found.
     */
    int VBVMR_Output_GetDeviceNumber();

    /**
     * Find an output device by index and maps it's type, name, and hardwareID to
     * pointers. All pointers can be null. See API PDF for more details.
     *
     * @param index      Zero-based index of the device
     * @param type       32bit long pointer that will receive the device type.
     * @param deviceName 32bit (minimum) string pointer that will receive the
     *                    device name.
     * @param hardwareId 32bit (minimum) string pointer that will receive the
     *                    hardware ID.
     * @return Status code
     *              0: OK (no error)
     */
    int VBVMR_Output_GetDeviceDescA(int index, Pointer type, Pointer deviceName, Pointer hardwareId);
    int VBVMR_Output_GetDeviceDescW(int index, Pointer type, Pointer deviceName, Pointer hardwareId);

    /**
     * Gets the number of audio input device available on the system
     *
     * @return Number of devices found.
     */
    int VBVMR_Input_GetDeviceNumber();

    /**
     * Finds a device by index and maps it's type, name, and hardwareID to
     * pointers. All pointers can be null. See API PDF for more details.
     *
     * @param index      Zero-based index of the device
     * @param type       32bit long pointer that will receive the device type
     * @param deviceName 32bit (minimum) string pointer that will receive the
     *                    device name
     * @param hardwareId 32bit (minimum) string pointer that will receive the
     *                    hardware ID.
     * @return Status code
     *              0: OK (no error)
     */
    int VBVMR_Input_GetDeviceDescA(int index, Pointer type, Pointer deviceName, Pointer hardwareId);
    int VBVMR_Input_GetDeviceDescW(int index, Pointer type, Pointer deviceName, Pointer hardwareId);


    //******************************************************************************//
    //*                             VB-AUDIO CALLBACK                              *//
    //******************************************************************************//

    // Command to initialize data according to SR and buffer size.
    // info = (VBVMR_LPT_AUDIOINFO) data
    int VBVMR_CBCOMMAND_STARTING = 1;

    // Command to release data
    int VBVMR_CBCOMMAND_ENDING = 2;

    // If there is a change in audio stream, you must restart audio.
    int VBVMR_CBCOMMAND_CHANGE = 3;

    // Input insert
    int VBVMR_CBCOMMAND_BUFFER_IN = 10;

    // Bus output insert
    int VBVMR_CBCOMMAND_BUFFER_OUT = 11;

    // All I/O
    // audiobuffer = (VBVMR_LPT_AUDIOBUFFER) data
    // nnn = 1 if syncronized with Voicemeeter
    int VBVMR_CBCOMMAND_BUFFER_MAIN = 20;

    // Process input insert
    int VBVMR_AUDIOCALLBACK_IN = 0x00000001;

    // Process output bus insert
    int VBVMR_AUDIOCALLBACK_OUT = 0x00000002;

    // Receive all I/O
    int VBVMR_AUDIOCALLBACK_MAIN = 0x00000004;

    /**
     * Register your audio callback function to receive real time audio buffer.
     * It's possible to register up to three different audio callbacks in the same
     * application or in three different applications. In the same application, it
     * is possible because Voicemeeter provides three kinds of audio streams:
     *      - Audio Input Insert: Processes all Voicemeeter inputs as insert
     *      - Audio Output Insert: Processes all Voicemeeter BUS outputs as insert
     *      - All Audio I/O: Processes all Voicemeeter I/O
     *
     * Note: A single callback can be used to receive the three possible audio
     * streams.
     *
     * @param mode       Callback type:
     *                        Input:  VBVMR_AUDIOCALLBACK_IN
     *                        Output: VBVMR_AUDIOCALLBACK_OUT
     *                        Main:   VBVMR_AUDIOCALLBACK_MAIN
     * @param callback   Pointer to your callback function
     * @param user       Pointer that will be passed to the callback function's
     *                        first arguments
     * @param clientName Input: Name of the application registering the callback
     *                    Output: Name of the application already registered
     * @return Status code:
     *              0:  OK (no error)
     *              -1: Error
     *              1:  Callback already registered by another application
     */
    int VBVMR_AudioCallbackRegister(int mode, T_VBVMR_VBAUDIOCALLBACK callback, Pointer user, char[] clientName);

    /**
     * Start or Stop calling the callback.
     *
     * @return Status code
     *              0:  OK (no error)
     *              -1: Error
     *              -2: No callback registered
     */
    int VBVMR_AudioCallbackStart();
    int VBVMR_AudioCallbackStop();

    /**
     * Unregister your callback and release the Voicemeeter virtual driver. This
     * function will automatically call VBVMR_AudioCallbackStop().
     *
     * @return Status Code
     *              0:  OK (no error)
     *              -1: Error
     *              1:  Callback already unregistered
     */
    int VBVMR_AudioCallbackUnregister();

    /**
     * VBAUDIOCALLBACK is called for different tasks to initialize, perform, and
     * end your process. It is part of a single time-critical thread.
     * It is non-re-entrant (meaning it cannot be called while in the process).
     *
     * Additionally, it must be real time when called to process buffer, meaning
     * that the process has to be performed as fast as possible. Waiting cycles
     * are forbidden. No not use OS synchronization objects. Do not use system
     * functions that can generate that can generate waiting cycles like display,
     * disk, or communication functions.
     */
    interface T_VBVMR_VBAUDIOCALLBACK extends StdCallCallback {
        /**
         * @param user    User pointer given on callback registration
         * @param command Reason why the callback is called
         * @param data    Pointer on structure, pending on command
         * @param nnn     Additional unused data
         * @return 0
         */
        boolean callback(Pointer user, int command, Pointer data, int nnn);
    }

    class tagVBVMR_AUDIOINFO extends Structure {
        public int samplerate;
        public int nbSamplePerFrame;

        protected List<String> getFieldOrder() {
            return Arrays.asList("samplerate", "nbSamplePerFrame");
        }
    }

    class tagVBVMR_AUDIOBUFFER extends Structure {
        // Sampling rate
        public int audiobuffer_sr;

        // Number of samples per frame
        public int audiobuffer_nbs;

        // Number of inputs
        public int audiobuffer_nbi;

        // Number of outputs
        public int audiobuffer_nbo;

        // NBI input pointers containing frames of nbs samples (32bit floats)
        public Pointer audiobuffer_r;

        // NBO output pointers containing frames of nbo samples (32bit floats)
        public Pointer audiobuffer_w;

        protected List<String> getFieldOrder() {
            return Arrays.asList("audiobuffer_sr", "audiobuffer_nbs", "audiobuffer_nbi", "audiobuffer_nbo", "audiobuffer_r", "audiobuffer_w");
        }
    }

    tagVBVMR_AUDIOINFO VBVMR_T_AUDIOINFO = new tagVBVMR_AUDIOINFO();
    Pointer VBVMR_PT_AUDIOINFO = new tagVBVMR_AUDIOINFO().getPointer();
    Pointer VBVBMR_LPT_AUDIOINFO = new tagVBVMR_AUDIOINFO().getPointer();

    tagVBVMR_AUDIOBUFFER VBVMR_T_AUDIOBUFFER = new tagVBVMR_AUDIOBUFFER();
    Pointer VBVMR_PT_AUDIOBUFFER = new tagVBVMR_AUDIOBUFFER().getPointer();
    Pointer VBVMR_LPT_AUDIOBUFFER = new tagVBVMR_AUDIOBUFFER().getPointer();



    //******************************************************************************//
    //*                          'C' STRUCTURED INTERFACE                          *//
    //******************************************************************************//

    interface T_VBVMR_Login extends StdCallCallback {
        boolean callback();
    }
    interface T_VBVMR_Logout extends StdCallCallback {
        boolean callback();
    }
    interface T_VBVMR_RunVoicemeeter extends StdCallCallback {
        boolean callback(int type);
    }

    interface T_VBVMR_GetVoicemeeterType extends StdCallCallback {
        boolean callback(Pointer type);
    }
    interface T_VBVMR_GetVoicemeeterVersion extends StdCallCallback {
        boolean callback(Pointer version);
    }

    interface T_VBVMR_IsParametersDirty extends StdCallCallback {
        boolean callback();
    }
    interface T_VBVMR_GetParameterFloat extends StdCallCallback {
        boolean callback(Pointer paramName, Pointer value);
    }
    interface T_VBVMR_GetParameterStringA extends StdCallCallback {
        boolean callback(Pointer paramName, Pointer string);
    }
    interface T_VBVMR_GetParameterStringW extends StdCallCallback {
        boolean callback(Pointer paramName, Pointer string);
    }

    interface T_VBVMR_GetLevel extends StdCallCallback {
        boolean callback(int type, int channel, Pointer value);
    }
    interface T_VBVMR_GetMidiMessage extends StdCallCallback {
        boolean callback(Pointer pMIDIBuffer, int byteMax);
    }

    interface T_VBVMR_SetParameterFloat extends StdCallCallback {
        boolean callback(Pointer paramName, int value);
    }
    interface T_VBVMR_SetParameters extends StdCallCallback {
        boolean callback(Pointer paramScript);
    }
    interface T_VBVMR_SetParametersW extends StdCallCallback {
        boolean callback(Pointer paramScript);
    }
    interface T_VBVMR_SetParameterStringA extends StdCallCallback {
        boolean callback(Pointer paramName, Pointer string);
    }
    interface T_VBVMR_SetParameterStringW extends StdCallCallback {
        boolean callback(Pointer paramName, Pointer string);
    }

    interface T_VBVMR_Output_GetDeviceNumber extends StdCallCallback {
        boolean callback();
    }
    interface T_VBVMR_Output_GetDeviceDescA extends StdCallCallback {
        boolean callback(int index, Pointer type, Pointer deviceName, Pointer hardwareId);
    }
    interface T_VBVMR_Output_GetDeviceDescW extends StdCallCallback {
        boolean callback(int index, Pointer type, Pointer deviceName, Pointer hardwareId);
    }
    interface T_VBVMR_Input_GetDeviceNumber extends StdCallCallback {
        boolean callback();
    }
    interface T_VBVMR_Input_GetDeviceDescA extends StdCallCallback {
        boolean callback(int index, Pointer type, Pointer deviceName, Pointer hardwareId);
    }
    interface T_VBVMR_Input_GetDeviceDescW extends StdCallCallback {
        boolean callback(int index, Pointer type, Pointer deviceName, Pointer hardwareId);
    }

    interface T_VBVMR_AudioCallbackRegister extends StdCallCallback {
        boolean callback(int mode, T_VBVMR_VBAUDIOCALLBACK callback, Pointer user, char[] clientName);
    }
    interface T_VBVMR_AudioCallbackStart extends StdCallCallback {
        boolean callback();
    }
    interface T_VBVMR_AudioCallbackStop extends StdCallCallback {
        boolean callback();
    }
    interface T_VBVMR_AudioCallbackUnregister extends StdCallCallback {
        boolean callback();
    }

    class tagVBVMR_INTERFACE extends Structure {
        public T_VBVMR_Login					VBVMR_Login;
        public T_VBVMR_Logout					VBVMR_Logout;
        public T_VBVMR_RunVoicemeeter			VBVMR_RunVoicemeeter;
        public T_VBVMR_GetVoicemeeterType       VBVMR_GetVoicemeeterType;
        public T_VBVMR_GetVoicemeeterVersion	VBVMR_GetVoicemeeterVersion;
        public T_VBVMR_IsParametersDirty		VBVMR_IsParametersDirty;
        public T_VBVMR_GetParameterFloat		VBVMR_GetParameterFloat;
        public T_VBVMR_GetParameterStringA		VBVMR_GetParameterStringA;
        public T_VBVMR_GetParameterStringW		VBVMR_GetParameterStringW;

        public T_VBVMR_GetLevel				    VBVMR_GetLevel;
        public T_VBVMR_GetMidiMessage			VBVMR_GetMidiMessage;

        public T_VBVMR_SetParameterFloat		VBVMR_SetParameterFloat;
        public T_VBVMR_SetParameters			VBVMR_SetParameters;
        public T_VBVMR_SetParametersW			VBVMR_SetParametersW;
        public T_VBVMR_SetParameterStringA		VBVMR_SetParameterStringA;
        public T_VBVMR_SetParameterStringW		VBVMR_SetParameterStringW;

        public T_VBVMR_Output_GetDeviceNumber	VBVMR_Output_GetDeviceNumber;
        public T_VBVMR_Output_GetDeviceDescA	VBVMR_Output_GetDeviceDescA;
        public T_VBVMR_Output_GetDeviceDescW	VBVMR_Output_GetDeviceDescW;
        public T_VBVMR_Input_GetDeviceNumber	VBVMR_Input_GetDeviceNumber;
        public T_VBVMR_Input_GetDeviceDescA	    VBVMR_Input_GetDeviceDescA;
        public T_VBVMR_Input_GetDeviceDescW	    VBVMR_Input_GetDeviceDescW;

        public T_VBVMR_AudioCallbackRegister	VBVMR_AudioCallbackRegister;
        public T_VBVMR_AudioCallbackStart		VBVMR_AudioCallbackStart;
        public T_VBVMR_AudioCallbackStop		VBVMR_AudioCallbackStop;
        public T_VBVMR_AudioCallbackUnregister	VBVMR_AudioCallbackUnregister;

        protected List<String> getFieldOrder() {
            return Arrays.asList(
                    "VBVMR_Login",
                    "VBVMR_Logout",
                    "VBVMR_RunVoicemeeter",
                    "VBVMR_GetVoicemeeterType",
                    "VBVMR_GetVoicemeeterVersion",
                    "VBVMR_IsParametersDirty",
                    "VBVMR_GetParameterFloat",
                    "VBVMR_GetParameterStringA",
                    "VBVMR_GetParameterStringW",
                    "VBVMR_GetLevel",
                    "VBVMR_GetMidiMessage",
                    "VBVMR_SetParameterFloat",
                    "VBVMR_SetParameters",
                    "VBVMR_SetParametersW",
                    "VBVMR_SetParameterStringA",
                    "VBVMR_SetParameterStringW",
                    "VBVMR_Output_GetDeviceNumber",
                    "VBVMR_Output_GetDeviceDescA",
                    "VBVMR_Output_GetDeviceDescW",
                    "VBVMR_Input_GetDeviceNumber",
                    "VBVMR_Input_GetDeviceDescA",
                    "VBVMR_Input_GetDeviceDescW",
                    "VBVMR_AudioCallbackRegister",
                    "VBVMR_AudioCallbackStart",
                    "VBVMR_AudioCallbackStop",
                    "VBVMR_AudioCallbackUnregister"
            );
        }
    }

    tagVBVMR_INTERFACE T_VBVMR_INTERFACE = new tagVBVMR_INTERFACE();
    Pointer PT_VBVMR_INTERFACE = new tagVBVMR_INTERFACE().getPointer();
    Pointer LPT_VBVMR_INTERFACE = new tagVBVMR_INTERFACE().getPointer();
}

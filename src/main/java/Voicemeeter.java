import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import lombok.Data;

@SuppressWarnings("Duplicates")
public class Voicemeeter {
    private static VoicemeeterInstance instance;

    public static String DEFAULT_VM_WINDOWS_64BIT_PATH = "C:/Program Files (x86)/VB/Voicemeeter/VoicemeeterRemote64.dll";
    public static String DEFAULT_VM_WINDOWS_32BIT_PATH = "C:/Program Files (x86)/VB/Voicemeeter/VoicemeeterRemote.dll";

    public static void init() {
        init(true);
    }

    public static void init(boolean is64bit) {
        init(is64bit, is64bit ? DEFAULT_VM_WINDOWS_64BIT_PATH : DEFAULT_VM_WINDOWS_32BIT_PATH);
    }

    public static void init(boolean is64bit, String vmWindowsPath) {
        System.load(vmWindowsPath);
        instance = Native.loadLibrary("VoicemeeterRemote" + (is64bit ? "64" : ""), VoicemeeterInstance.class);
    }

    public static void init(VoicemeeterInstance voicemeeterInstance) {
        instance = voicemeeterInstance;
    }

    public static void login() throws VoicemeeterException {
        int val = instance.VBVMR_Login();
        switch (val) {
            case 0:
                break;
            case 1:
                throw new VoicemeeterException("Voicemeeter is not open");
            case -1:
                throw new VoicemeeterException("Unable to get the Voicemeeter client");
            case -2:
                throw new VoicemeeterException("Unexpected login (The client is already logged in)");
            default:
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }
    }

    public static void logout() {
        int val = instance.VBVMR_Logout();
        if (val != 0) {
            throw new VoicemeeterException("Unexpected function reutrn value. Function returned " + val);
        }
    }

    public static void runVoicemeeter(int type) {
        int val = instance.VBVMR_RunVoicemeeter(type);
        switch (val) {
            case 0:
                break;
            case -1:
                throw new VoicemeeterException("Voicemeeter is not installed");
            default:
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }
    }

    public static int getVoicemeeterType() {
        Pointer type = getPointer(4);
        int val = instance.VBVMR_GetVoicemeeterType(type);
        switch (val) {
            case 0:
                return type.getInt(0);
            case -1:
                throw new VoicemeeterException("Unable to get the Voicemeeter client");
            case -2:
                throw new VoicemeeterException("Unable to get the Voicemeeter server");
            default:
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }
    }

    public static int getVoicemeeterVersion() {
        Pointer version = getPointer(4);
        int val = instance.VBVMR_GetVoicemeeterVersion(version);
        switch (val) {
            case 0:
                return version.getInt(0);
            case -1:
                throw new VoicemeeterException("Unable to get the Voicemeeter client");
            case -2:
                throw new VoicemeeterException("Unable to get the Voicemeeter server");
            default:
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }
    }

    public static boolean areParametersDirty() {
        int val = instance.VBVMR_IsParametersDirty();
        switch (val) {
            case 0:
                return false;
            case 1:
                return true;
            case -1:
                throw new VoicemeeterException("An error has occurred");
            case -2:
                throw new VoicemeeterException("Unable to get the Voicemeeter server");
            default:
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }
    }

    public static float getParameterFloat(String parameterName) {
        Pointer paramName = getStringPointer(parameterName);
        Pointer paramValue = getPointer(4);
        int val = instance.VBVMR_GetParameterFloat(paramName, paramValue);

        switch (val) {
            case 0:
                return paramValue.getFloat(0);
            case -1:
                throw new VoicemeeterException("An error has occurred");
            case -2:
                throw new VoicemeeterException("Unable to get the Voicemeeter server");
            case -3:
                throw new VoicemeeterException("Unknown parameter name");
            case -5:
                throw new VoicemeeterException("Structure mismatch");
            default:
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }
    }

    public static String getParameterStringA(String parameterName) {
        Pointer paramName = getStringPointer(parameterName);
        Pointer paramValue = getPointer(8);
        int val = instance.VBVMR_GetParameterStringA(paramName, paramValue);

        switch (val) {
            case 0:
                return paramValue.getString(0);
            case -1:
                throw new VoicemeeterException("An error has occurred");
            case -2:
                throw new VoicemeeterException("Unable to get the Voicemeeter server");
            case -3:
                throw new VoicemeeterException("Unknown parameter name");
            case -5:
                throw new VoicemeeterException("Structure mismatch");
            default:
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }
    }

    public static String getParameterStringW(String parameterName) {
        Pointer paramName = getStringPointer(parameterName);
        Pointer paramValue = getPointer(8);
        int val = instance.VBVMR_GetParameterStringW(paramName, paramValue);

        switch (val) {
            case 0:
                return paramValue.getString(0);
            case -1:
                throw new VoicemeeterException("An error has occurred");
            case -2:
                throw new VoicemeeterException("Unable to get the Voicemeeter server");
            case -3:
                throw new VoicemeeterException("Unknown parameter name");
            case -5:
                throw new VoicemeeterException("Structure mismatch");
            default:
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }
    }

    public static float getLevel(int type, int channel) {
        Pointer levelValue = getPointer(4);
        int val = instance.VBVMR_GetLevel(type, channel, levelValue);

        switch (val) {
            case 0:
                return levelValue.getFloat(0);
            case -1:
                throw new VoicemeeterException("An error has occurred");
            case -2:
                throw new VoicemeeterException("Unable to get the Voicemeeter server");
            case -3:
                throw new VoicemeeterException("No level value available");
            case -4:
                throw new VoicemeeterException("The type of the channel is outside of the allowed range");
            default:
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }
    }

    public static byte[] getMidiMessage(int size) {
        Pointer midiMessage = getPointer(size);
        int val = instance.VBVMR_GetMidiMessage(midiMessage, size);

        switch (val) {
            case -1:
                throw new VoicemeeterException("An error has occurred");
            case -2:
                throw new VoicemeeterException("Unable to get the Voicemeeter server");
            case -5:
            case -6:
                throw new VoicemeeterException("No MIDI data available");
            default:
                if (val >= 0)
                    return midiMessage.getByteArray(0, size);
                else
                    throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }
    }

    public static void setParameterFloat(String parameterName, float value) {
        Pointer paramName = getStringPointer(parameterName);
        int val = instance.VBVMR_SetParameterFloat(paramName, value);

        switch (val) {
            case 0:
                break;
            case -1:
                throw new VoicemeeterException("An error has occurred");
            case -2:
                throw new VoicemeeterException("Unable to get the Voicemeeter server");
            case -3:
                throw new VoicemeeterException("Unknown parameter name");
            default:
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }
    }

    public static void setParameterStringA(String parameterName, String value) {
        Pointer paramName = getStringPointer(parameterName);
        Pointer paramValue = getStringPointer(value);
        int val = instance.VBVMR_SetParameterStringA(paramName, paramValue);

        switch (val) {
            case 0:
                break;
            case -1:
                throw new VoicemeeterException("An error has occurred");
            case -2:
                throw new VoicemeeterException("Unable to get the Voicemeeter server");
            case -3:
                throw new VoicemeeterException("Unknown parameter name");
            default:
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }
    }

    public static void setParameterStringW(String parameterName, String value) {
        Pointer paramName = getStringPointer(parameterName);
        Pointer paramValue = getStringPointer(value);
        int val = instance.VBVMR_SetParameterStringW(paramName, paramValue);

        switch (val) {
            case 0:
                break;
            case -1:
                throw new VoicemeeterException("An error has occurred");
            case -2:
                throw new VoicemeeterException("Unable to get the Voicemeeter server");
            case -3:
                throw new VoicemeeterException("Unknown parameter name");
            default:
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }
    }

    public static void setParameters(String script) {
        Pointer stringPointer = getStringPointer(script);
        int val = instance.VBVMR_SetParameters(stringPointer);

        switch (val) {
            case 0:
                break;
            case -1:
            case -3:
            case -4:
                throw new VoicemeeterException("An error has occurred");
            case -2:
                throw new VoicemeeterException("Unable to get the Voicemeeter server");
            default:
                if (val > 0)
                    throw new VoicemeeterException("Script error on line " + val);
                else
                    throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }
    }

    public static void setParametersW(String script) {
        Pointer stringPointer = getStringPointer(script);
        int val = instance.VBVMR_SetParametersW(stringPointer);

        switch (val) {
            case 0:
                break;
            case -1:
            case -3:
            case -4:
                throw new VoicemeeterException("An error has occurred");
            case -2:
                throw new VoicemeeterException("Unable to get the Voicemeeter server");
            default:
                if (val > 0)
                    throw new VoicemeeterException("Script error on line " + val);
                else
                    throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }
    }

    public static int getNumberOfAudioDevices(boolean areInputDevices) {
        if (areInputDevices) {
            return instance.VBVMR_Input_GetDeviceNumber();
        } else {
            return instance.VBVMR_Output_GetDeviceNumber();
        }
    }

    public static DeviceDescription getAudioDeviceDescriptionA(int index, boolean isInputDevice) {
        int val;
        Pointer type = getPointer(4);
        Pointer name = getPointer(4);
        Pointer hardwareId = getPointer(4);

        if (isInputDevice) {
            val = instance.VBVMR_Input_GetDeviceDescA(index, type, name, hardwareId);
            if (val != 0)
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        } else {
            val = instance.VBVMR_Output_GetDeviceDescA(index, type, name, hardwareId);
            if (val != 0)
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }

        DeviceDescription desc = new DeviceDescription();
        desc.setType(type.getInt(0));
        desc.setName(name.getString(0));
        desc.setHardwareId(hardwareId.getString(0));

        return desc;
    }

    public static DeviceDescription getOutputDeviceDescriptionW(int index, boolean isInputDevice) {
        int val;
        Pointer type = getPointer(4);
        Pointer name = getPointer(4);
        Pointer hardwareId = getPointer(4);

        if (isInputDevice) {
            val = instance.VBVMR_Input_GetDeviceDescW(index, type, name, hardwareId);
            if (val != 0)
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        } else {
            val = instance.VBVMR_Output_GetDeviceDescW(index, type, name, hardwareId);
            if (val != 0)
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }

        DeviceDescription desc = new DeviceDescription();
        desc.setType(type.getInt(0));
        desc.setName(name.getString(0));
        desc.setHardwareId(hardwareId.getString(0));

        return desc;
    }

    @Data
    private static class DeviceDescription {
        private int type;
        private String name;
        private String hardwareId;
    }

    private static Pointer getStringPointer(String str) {
        int size = str.getBytes().length + 1;
        Memory m = new Memory(size);
        m.setString(0, str);
        return m;
    }

    private static Pointer getPointer(int size) {
        return new Memory(size);
    }
}

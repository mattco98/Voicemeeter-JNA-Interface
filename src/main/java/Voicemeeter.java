import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import lombok.Data;

@SuppressWarnings("Duplicates")
public class Voicemeeter {
    private VoicemeeterInstance instance;

    public String DEFAULT_VM_WINDOWS_64BIT_PATH = "C:/Program Files (x86)/VB/Voicemeeter/VoicemeeterRemote64.dll";
    public String DEFAULT_VM_WINDOWS_32BIT_PATH = "C:/Program Files (x86)/VB/Voicemeeter/VoicemeeterRemote.dll";

    public Voicemeeter(String voicemeeterRemoteDllPath, boolean is64Bit) {
        System.load(voicemeeterRemoteDllPath);
        instance = Native.loadLibrary("VoicemeeterRemote" + (is64Bit ? "64" : ""), VoicemeeterInstance.class);
    }

    public Voicemeeter(VoicemeeterInstance voicemeeterInstance) {
        instance = voicemeeterInstance;
    }

    public void login() throws VoicemeeterException {
        int val;
        switch (val = instance.VBVMR_Login()) {
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

    public void logout() {
        int val;
        if ((val = instance.VBVMR_Logout()) != 0) {
            throw new VoicemeeterException("Unexpected function reutrn value. Function returned " + val);
        }
    }

    public void runVoicemeeter(int type) {
        int val;
        switch (val = instance.VBVMR_RunVoicemeeter(type)) {
            case 0:
                break;
            case -1:
                throw new VoicemeeterException("Voicemeeter is not installed");
            default:
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
        }
    }

    public int getVoicemeeterType() {
        int val;
        Pointer type = getPointer(4);
        switch (val = instance.VBVMR_GetVoicemeeterType(type)) {
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

    public int getVoicemeeterVersion() {
        int val;
        Pointer version = getPointer(4);
        switch (val = instance.VBVMR_GetVoicemeeterVersion(version)) {
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

    public boolean areParametersDirty() {
        int val;
        switch (val = instance.VBVMR_IsParametersDirty()) {
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

    public float getParameterFloat(String parameterName) {
        int val;
        Pointer paramName = getStringPointer(parameterName);
        Pointer paramValue = getPointer(4);

        switch (val = instance.VBVMR_GetParameterFloat(paramName, paramValue)) {
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

    public String getParameterStringA(String parameterName) {
        int val;
        Pointer paramName = getStringPointer(parameterName);
        Pointer paramValue = getPointer(8);

        switch (val = instance.VBVMR_GetParameterStringA(paramName, paramValue)) {
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

    public String getParameterStringW(String parameterName) {
        int val;
        Pointer paramName = getStringPointer(parameterName);
        Pointer paramValue = getPointer(8);

        switch (val = instance.VBVMR_GetParameterStringW(paramName, paramValue)) {
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

    public float getLevel(int type, int channel) {
        int val;
        Pointer levelValue = getPointer(4);

        switch (val = instance.VBVMR_GetLevel(type, channel, levelValue)) {
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

    public byte[] getMidiMessage(int size) {
        int val;
        Pointer midiMessage = getPointer(size);

        switch (val = instance.VBVMR_GetMidiMessage(midiMessage, size)) {
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

    public void setParameterFloat(String parameterName, float value) {
        int val;
        Pointer paramName = getStringPointer(parameterName);

        switch (val = instance.VBVMR_SetParameterFloat(paramName, value)) {
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

    public void setParameterStringA(String parameterName, String value) {
        int val;
        Pointer paramName = getStringPointer(parameterName);
        Pointer paramValue = getStringPointer(value);

        switch (val = instance.VBVMR_SetParameterStringA(paramName, paramValue)) {
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

    public void setParameterStringW(String parameterName, String value) {
        int val;
        Pointer paramName = getStringPointer(parameterName);
        Pointer paramValue = getStringPointer(value);

        switch (val = instance.VBVMR_SetParameterStringW(paramName, paramValue)) {
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

    public void setParameters(String script) {
        int val;
        Pointer scriptP = getStringPointer(script);

        switch (val = instance.VBVMR_SetParameters(scriptP)) {
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

    public void setParametersW(String script) {
        int val;
        Pointer scriptP = getStringPointer(script);

        switch (val = instance.VBVMR_SetParametersW(scriptP)) {
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

    public int getNumberOfAudioDevices(boolean areInputDevices) {
        if (areInputDevices) {
            return instance.VBVMR_Input_GetDeviceNumber();
        } else {
            return instance.VBVMR_Output_GetDeviceNumber();
        }
    }

    public DeviceDescription getAudioDeviceDescriptionA(int index, boolean isInputDevice) {
        int val;
        Pointer type = getPointer(4);
        Pointer name = getPointer(4);
        Pointer hardwareId = getPointer(4);

        if (isInputDevice) {
            if ((val = instance.VBVMR_Input_GetDeviceDescA(index, type, name, hardwareId)) != 0) {
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
            }
        } else {
            if ((val = instance.VBVMR_Output_GetDeviceDescA(index, type, name, hardwareId)) != 0) {
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
            }
        }

        DeviceDescription desc = new DeviceDescription();
        desc.setType(type.getInt(0));
        desc.setName(name.getString(0));
        desc.setHardwareId(hardwareId.getString(0));

        return desc;
    }

    public DeviceDescription getOutputDeviceDescriptionW(int index, boolean isInputDevice) {
        int val;
        Pointer type = getPointer(4);
        Pointer name = getPointer(4);
        Pointer hardwareId = getPointer(4);

        if (isInputDevice) {
            if ((val = instance.VBVMR_Input_GetDeviceDescW(index, type, name, hardwareId)) != 0) {
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
            }
        } else {
            if ((val = instance.VBVMR_Output_GetDeviceDescW(index, type, name, hardwareId)) != 0) {
                throw new VoicemeeterException("Unexpected function return value. Function returned " + val);
            }
        }

        DeviceDescription desc = new DeviceDescription();
        desc.setType(type.getInt(0));
        desc.setName(name.getString(0));
        desc.setHardwareId(hardwareId.getString(0));

        return desc;
    }

    @Data
    private class DeviceDescription {
        private int type;
        private String name;
        private String hardwareId;
    }

    private Pointer getStringPointer(String str) {
        int size = str.getBytes().length + 1;
        Memory m = new Memory(size);
        m.setString(0, str);
        return m;
    }

    private Pointer getPointer(int size) {
        return new Memory(size);
    }
}
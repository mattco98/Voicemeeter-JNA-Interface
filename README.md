## Voicemeeter Interface for Java using JNA

This is an interface written with JNA to control Voicemeeter (and Voicemeeter 
Banana) via it's API. The .dll file itself comes pre-installed with Voicemeeter,
however the files I based this interface off of come with the 
VoicemeeterRemoteAPIPack (version 6). The RemoteAPIPack can be downloaded from the 
[Voicemeeter API forum page](https://forum.vb-audio.com/viewtopic.php?f=8&t=346&sid=74a4f83ebfdb023cb2bf544f7f80827d). 

#### Examples

I created this in order to automate the changing of certain values under certain 
conditions. Specifically, I wanted to change the strip compression and gain values
when I had a certain program open, and return the values to default when I closed the
program. Here is an example of such a program using this interface:

```java
public class ProgramController {
    private Voicemeeter vm;
    
    private final String programName = "program.exe";
    private final float strip0GainDefault = 0.0f;
    private final float strip0CompDefault = 0.0f;
    private final float strip0GainProgramOpen = -6.0f;
    private final float strip0CompProgramOpen = 4.5f;
    
    private boolean programOpen = false;

    public static void main(String[] args) {
        new ProgramController().init();
    }

    private void init() {
        vm = new Voicemeeter(Voicemeeter.DEFAULT_VM_WINDOWS_64BIT_PATH, true);

        whileLoop:
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (String str : getOpenPrograms()) {
                if (str.equals(programName)) {
                    if (!programOpen) switchToProgramOpen();
                    continue whileLoop;
                }
            }

            if (programOpen) switchToDefault();
        }
    }

    private void switchToProgramOpen() {
        programOpen = true;

        vm.setParameterFloat("Strip[0].gain", strip0GainProgramOpen);
        vm.setParameterFloat("Strip[0].comp", strip0CompProgramOpen);
    }

    private void switchToDefault() {
        programOpen = false;

        vm.setParameterFloat("Strip[0].gain", strip0GainDefault);
        vm.setParameterFloat("Strip[0].comp", strip0CompDefault);
    }

    private String[] getOpenPrograms() {
        // Return array of program names. Implementation will differ between 
        // different operating systems. 
    }
}
```

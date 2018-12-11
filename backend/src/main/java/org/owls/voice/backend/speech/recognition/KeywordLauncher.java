package org.owls.voice.backend.speech.recognition;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import org.owls.voice.backend.plugins.PluginController;
import org.owls.voice.plugins.api.SpeechSynthesizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

@Component
public class KeywordLauncher {

    @Autowired
    SpeechSynthesizer speechSynthesizer;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    PluginController pluginController;

    private Configuration configuration;

    private static final Logger log = LoggerFactory.getLogger(KeywordLauncher.class);

    @Value("${sphinx.path.dictionary}")
    private String dictionaryPath;

    @Value("${sphinx.path.acoustic.model}")
    private String acousticModelPath;

    @Value("${sphinx.path.language.model}")
    private String languageModelPath;

    public KeywordLauncher() {

    }

    @PostConstruct
    public void initialize() {
        try {
            // setup configuration
            configuration = new Configuration();
            configuration.setAcousticModelPath(acousticModelPath);
            configuration.setDictionaryPath(dictionaryPath);
            configuration.setLanguageModelPath(languageModelPath);

            // play bootup sound
            File bootUpSoundFile = resourceLoader.getResource("classpath:boot.wav").getFile();
            Clip audioClip = AudioSystem.getClip();
            audioClip.open(AudioSystem.getAudioInputStream(bootUpSoundFile));
            audioClip.start();

        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }

        log.info(getClass().getSimpleName() + " has been started!");

    }

    public void listenForKeyword() throws IOException {

        //Recognizer object, Pass the Configuration object
        LiveSpeechRecognizer recognize = new LiveSpeechRecognizer(configuration);

        //Start Recognition Process (The bool parameter clears the previous cache if true)
        recognize.startRecognition(true);

        //Creating SpeechResult object
        SpeechResult result;

        //Check if recognizer recognized the speech
        log.info("Waiting for recognizable words ...");
        while ((result = recognize.getResult()) != null) {

            //Get the recognized speech
            String command = result.getHypothesis();
            command = command.toLowerCase();
            String work = null;
            Process p;

            log.info(command);

            /*
            if (command != null && commandMap.contains(command)) {
                // commandMap.get(command).execute();
                // TODO: re-implemente execute functionality
                // commandMap.get(commandMap.indexOf(command)).execute();
            }
            */
        }
    }

}

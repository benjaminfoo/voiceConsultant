package org.owls.voice.webapp.controller;

import org.owls.voice.backend.commands.Command;
import org.owls.voice.backend.model.VoiceCommand;
import org.owls.voice.backend.persistance.ServiceLoadController;
import org.owls.voice.backend.persistance.VoiceCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PluginController {

    @Autowired
    private ServiceLoadController serviceLoadController;

    @GetMapping("/plugins")
    public String listPlugins(Model model) {
        System.out.println("Serving plugins ... / ... " );

        System.out.println("model is null ? " + (model == null ? "true" : "false"));

        Iterable<Command> attributeValue = serviceLoadController.getPlugins();
        attributeValue.forEach(vc -> {
            System.out.println("MODEL: "+ vc.toString());
        });
        model.addAttribute("voiceCommands", attributeValue);
        model.addAttribute("voiceCount", ((List<Command>) attributeValue).size());

        return "plugins";
    }

}
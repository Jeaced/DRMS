package app.web.controllers;

import app.core.services.interfaces.ResourceService;
import app.web.DTO.ResourceDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StatsController {
    private final static Logger log = LogManager.getLogger(DashboardController.class);
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    ResourceService resourceService;

    @GetMapping({"/stats"})
    String getDashboard(Model model) {
        log.info("GET request /stats");

        List<ResourceDTO> resources = resourceService.findAll()
                .stream()
                .map(resource -> modelMapper.map(resource, ResourceDTO.class))
                .collect(Collectors.toList());

        model.addAttribute("resources", resources);

        return "stats";
    }
}

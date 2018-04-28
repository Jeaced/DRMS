package app.web.controllers;

import app.core.services.interfaces.ResourceService;
import app.web.DTO.ResourceDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @PostMapping("/stats/download")
    String getData(@RequestAttribute("resources") List<ResourceDTO> resources, HttpServletResponse response)  {
        response.setContentType("text/plain");
        String fileName = LocalDateTime.now() + "_stats";
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".txt");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONArray jsonArray = new JSONArray();
        for (ResourceDTO o : resources) {
            jsonArray.put(toJsonObject(o));
        }
        String a = null;
        try {
            a = jsonArray.toString(1);
            out.println(a);
            out.flush();
            out.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "stats";
    }

    JSONObject toJsonObject(ResourceDTO obj) {
        JSONObject o = new JSONObject();
        try {
            o.put("id", obj.getId());
            o.put("name", obj.getName());
            o.put("value", obj.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return o;
    }
}

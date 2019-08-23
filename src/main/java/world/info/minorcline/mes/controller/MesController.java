package world.info.minorcline.mes.controller;

import world.info.minorcline.mes.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.jr.ob.*;


@Controller
public class MesController {
	private int sizeX = 20, sizeY = 20;
	private String result = "";
	private String input = "";
	private double density = 0.1;
	private double flocculation = 0.4;
	private String[] runInfo;
	
	@RequestMapping("/")
	public String displayApplication(ModelMap model) {
		model.addAttribute("sizeX", sizeX);
		model.addAttribute("sizeY", sizeY);
		model.addAttribute("resultPointSet", result);
		model.addAttribute("inputPointSet", input);
		model.addAttribute("density", density + "");
		model.addAttribute("flocculation", flocculation + "");
		if (runInfo != null && runInfo.length > 0)
			model.addAttribute("runInfo", runInfo);
		return "mes";
	}
	
	
	@RequestMapping(path="/randomize", method=RequestMethod.GET)
	public String getRandomData(@RequestParam double d, @RequestParam double f) {
		density = d;
		flocculation = f;
		FlocculatedPointGenerator gen = new FlocculatedPointGenerator(new PointSet(sizeX - 1, sizeY - 1),density, flocculation);
		gen.generate();
		input = getJSONFromPointList(gen.getPointList());
		result = "";
		runInfo = null;
		return "redirect:/";
	}
	
	@RequestMapping(path="/calculate", method=RequestMethod.POST)
	public String runAlgorithm(@RequestParam String pointsInput, RedirectAttributes flash) {
		input = pointsInput;
		List<Point> inputPts = getPointListFromJSON(pointsInput);
		
		long start, finish;
		start = System.nanoTime();
		MinimalEnclosingShape mes = new MinimalEnclosingShape(inputPts);
		mes.findShape();
		finish = System.nanoTime();
		
		List<Point> resultList = mes.getResultList();
		result = getJSONFromPointList(resultList);
		
		String[] info = {
			String.format("Algorithm completed in %.3f ms.", (float)((finish - start)/1e6)),
			"Original number of points given was " + inputPts.size(),
			"Final number of points given was " + resultList.size(),
			"Difference: " + (resultList.size() - inputPts.size())
		};
		runInfo = info;
		
		return "redirect:/";
	}
	
	private List<Point> getPointListFromJSON(String json){
		List<Point> pointlist = new ArrayList<>();
		
		try {
			List<PointBean> beanList = JSON.std.listOfFrom(PointBean.class, json);
			for (PointBean bean : beanList) {
				pointlist.add(new Point(bean.getX(), bean.getY()));
			}
		} catch (Exception e) {
		}
		return pointlist;
	}
	
	private String getJSONFromPointList(List<Point> pointset) {
		String json = null;
		List<PointBean> beanlist = new ArrayList<PointBean>();
		for (Point p : pointset) {
			PointBean bean = new PointBean();
			bean.setX(p.x());
			bean.setY(p.y());
			beanlist.add(bean);
		}
		try {
			json = JSON.std.asString(beanlist);
		} catch (Exception e) {
			json = "";
		}
		return json;
	}
	
	
	
}

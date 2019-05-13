package com.cognizant.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.cognizant.beans.Layers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class MainActivity {
	static String htmlContent = " ";
	static String cssContent = " ";
	static Stack<String> stack = new Stack<String>();

	public static void main(String[] args) throws FileNotFoundException {

		// Reading JSON File using GSON
		Gson gson = new Gson();
		String path = "D:\\PROJECT\\Json files\\demo.json";
		BufferedReader br = new BufferedReader(new FileReader(path));
		Map<String, Layers> decoded = gson.fromJson(br, new TypeToken<Map<String,Layers>>(){}.getType());


		//Creating HTML and  CSS File
		try
		{
			File htmlFile = new File("D:\\PROJECT\\Copy of Automatic_HTML_Generater\\generatedHTML.html");
			File cssFile = new File("D:\\PROJECT\\Copy of Automatic_HTML_Generater\\generatedCSS.css");


			if(htmlFile.createNewFile() && cssFile.createNewFile())
			{

				// Writing HTML and CSS File
				FileWriter htmlWriter = new FileWriter(htmlFile);
				FileWriter cssWriter = new FileWriter(cssFile);
				BufferedWriter htmlBuffer = new BufferedWriter(htmlWriter);
				BufferedWriter cssBuffer = new BufferedWriter(cssWriter);
				htmlBuffer.write("<html>\n<head>\n<link rel = \"stylesheet\" type = \"text/css\" href = \"generatedCSS.css\">\n<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0\"></head>\n<body>\n");
				for(String layerName : decoded.keySet())
				{

					Layers layerValue = decoded.get(layerName);
					List<String> layerParent = layerValue.getParent();
					Map<String,String> cssStyles = layerValue.getCssStyles();
					for(String parent : layerParent)
					{
						//Printing outer layers
						htmlContent = "<div id = \"" + layerValue.getElementName() + "\"></div>\n";
						cssContent = "#"+ layerValue.getElementName()+ "{\nposition:absolute;\nborder:solid 1px black;\n";
						cssContent = cssContent + "height:" + layerValue.getHeight() +";\n"  ;
						cssContent = cssContent + "width:" +layerValue.getWidth() + ";\n" ;
						cssContent = cssContent + "left:" +layerValue.getStyleGuideX()+ ";\n" ;
						cssContent = cssContent + "top:" + layerValue.getStyleGuideY() + ";\n";
						for(String map : cssStyles.keySet())
						{
								cssContent = cssContent + map +":"+ cssStyles.get(map)+";\n";
						}
						cssContent = cssContent + "}\n";
						htmlBuffer.write(htmlContent);
						cssBuffer.write(cssContent);

					if(parent.equals("ROOT_REF_WINDOW"))
						{
							  htmlContent = htmlContent + "\n<div id = \"" + layerValue.getElementName() + "\">\n";
							  List<String> layerContains = layerValue.getContains();
							  if(layerContains.isEmpty())
							  {
								  htmlContent = htmlContent + "\n</div>";
							  }
							  else
							  {
							  for(String cont : layerContains)
							  {
								  	stack.push(cont);
							  }
							  //System.out.println(stack);
							  pushFunction(decoded,stack);
							  }
					}
					}
				}
				htmlBuffer.write(htmlContent);
				htmlBuffer.write("\n</body>\n</html>");
				//File Close
				htmlBuffer.close();
				cssBuffer.close();
			}
			else
			{
				System.out.println("file already exists");
			}
		}catch(Exception e)
		{e.printStackTrace();}
	}

	private static void pushFunction(Map<String, Layers> decoded, Stack<String> stack2) {

		while(!stack2.empty())
		{
			if(stack2.peek().equals("-1"))
			{
				htmlContent = htmlContent + "\n</div>";
				stack2.pop();

			}
			else
			{
			Layers subLayer = decoded.get(stack2.peek());
			List<String> subLayerContains = subLayer.getContains();
			if(subLayerContains.isEmpty())
			{
				htmlContent = htmlContent + "\n<div id = \"" + subLayer.getElementName() + "\"></div>\n";
				stack2.pop();
			}
			else
			{
				htmlContent = htmlContent + "\n<div id = \"" + subLayer.getElementName() + "\">";
				stack2.pop();
				stack2.push("-1");
				for(String contain : subLayerContains)
				{
					stack2.push(contain);
				}
			}
			}
		}





	}



}

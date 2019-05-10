package com.cognizant.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import com.cognizant.beans.Layers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class MainActivity {

	public static void main(String[] args) throws FileNotFoundException {

		// Reading JSON File using GSON
		Gson gson = new Gson();
		String path = "D:\\PROJECT\\Json files\\demo.json";
		BufferedReader br = new BufferedReader(new FileReader(path));
		Map<String, Layers> decoded = gson.fromJson(br, new TypeToken<Map<String,Layers>>(){}.getType());


		//Creating HTML and  CSS File
		try
		{
			File htmlFile = new File("D:\\PROJECT\\Automatic_HTML_Generater\\generatedHTML.html");
			File cssFile = new File("D:\\PROJECT\\Automatic_HTML_Generater\\generatedCSS.css");

			String htmlContent = " ";
			String cssContent = " ";
			if(htmlFile.createNewFile() && cssFile.createNewFile())
			{

				// Writing HTML and CSS File
				FileWriter htmlWriter = new FileWriter(htmlFile);
				FileWriter cssWriter = new FileWriter(cssFile);
				BufferedWriter htmlBuffer = new BufferedWriter(htmlWriter);
				BufferedWriter cssBuffer = new BufferedWriter(cssWriter);
				htmlBuffer.write("<html>\n<head>\n<link rel = \"stylesheet\" type = \"text/css\" href = \"generatedCSS.css\">\n</head>\n<body>\n");
				for(String layerName : decoded.keySet())
				{
					Layers layerValue = decoded.get(layerName);
					List<String> layerParent = layerValue.getParent();
					//Map<String,String> cssStyles = layerValue.getCssStyles();
					for(String parent : layerParent)
					{
						//Printing outer layers
					if(parent.equals("ROOT_REF_WINDOW"))
						{
							  //  Map<String,String> cssStyles = layerValue.getCssStyles();
								htmlContent = "<div id = \"" + layerValue.getElementName() + "\"></div>\n";
								cssContent = "#"+ layerValue.getElementName()+ "{\nposition:absolute;\nborder:solid 1px black;\n";
								cssContent = cssContent + "height:" + layerValue.getHeight() +";\n"  ;
								cssContent = cssContent + "width:" +layerValue.getWidth() + ";\n" ;
								cssContent = cssContent + "left:" +layerValue.getStyleGuideX()+ ";\n" ;
								cssContent = cssContent + "top:" + layerValue.getStyleGuideY() + ";\n";
								/*for(String map : cssStyles.keySet())
								{
										cssContent = cssContent + map +":"+ cssStyles.get(map)+";\n";
								}*/
								cssContent = cssContent + "}\n";
								htmlBuffer.write(htmlContent);
								cssBuffer.write(cssContent);
					}
					}
				}
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

}

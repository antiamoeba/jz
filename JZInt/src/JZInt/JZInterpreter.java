package JZInt;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class JZInterpreter {

	public static void main(String[] args) {
		File file = new File(args[0]);
		JZ jz = new JZ();
		String filename = file.getAbsolutePath();
		String name = file.getName();
		if(!filename.endsWith(".jz"))
		{
			filename += ".jz";
		}
		String output = "";
		try
		{
			String s;
			FileReader in = new FileReader(file);
			BufferedReader read = new BufferedReader(in);
			while((s=read.readLine())!=null)
			{
				if(s.length()>0)
				{
					output += s + "\n";
				}
			}
			read.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		jz.interpret(output);
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		String path = filename;
		if(filename.endsWith(".jz"))
		{
			path = filename.substring(0, filename.length()-3) + ".java";
		}
		System.out.println("Compiling " +  filename);
		OutputStream out = new ByteArrayOutputStream();
		if(compiler.run(null, null, out, path) != 0)
		{
			System.out.println(out.toString());
			System.out.println("finished");
		}
		else
		{
			if(path.endsWith(".java"))
			{
				path = path.substring(0, path.length()-5);
			}
			try
			{
				Process pr = null;
				String[] list = {"java", name};
				ProcessBuilder pb = new ProcessBuilder(list);
				pb.directory(new File(path.substring(0,path.length()-name.length())));
				pr = pb.start();
				System.out.println("Running " + name + ":\n\n");
				BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				BufferedReader error = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
				String s = "";
				String line = "";
				while((line=input.readLine())!=null)
				{
					s += line + "\n";
				}
				while((line=error.readLine())!=null)
				{
					s += line + "\n";
				}
				input.close();
				System.out.println(s);
				System.out.println("finished");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

}

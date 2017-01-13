package JZInt;
import java.util.ArrayList;

public class JZ {
	private int z;
	private int parseLength;
	ArrayList<String> jv;
	ArrayList<String> text;
	public ArrayList<String> interpret(String file)
	{
		text = new ArrayList<String>();
		jv = new ArrayList<String>();
		String[] temp = file.split("\n");
		for(int i = 0; i<temp.length;i++)
		{
			text.add(temp[i].trim());
		}
		for(int i=0;i<text.size(); i++)
		{
			try {
				String f = text.get(i);
				if(f.length()>0)
				{
					if(f.startsWith("Create a"))
					{
						String r = remove(f, "Create a");
						if(parse(r, "method of type")!=-1)
						{
							String s = r.substring(0,z).toLowerCase().trim();
							String[] o = r.substring(z+parseLength).split("called");
							s+=" " + o[0].trim();
							String[] p = o[1].split("with params");
							s+=" " + p[0].trim()+p[1].trim();
							String v = text.get(i).replace(f, "");
							jv.add(v + s.trim());
						}
						if(parse(r, "class")!=-1)
						{
							String s = r.substring(0,z).toLowerCase().trim();
							String[] o = r.substring(z+parseLength).split("called");
							s+=" class " + o[1].trim();
							String v = text.get(i).replace(f, "");
							jv.add(v + s.trim());
						}
						if(parse(r, "variable of type")!=-1)
						{
							String s = r.substring(0,z).toLowerCase().trim();
							String[] o = r.substring(z+parseLength).split("called");
							s+=" " + o[0].trim();
							String[] p = o[1].split("with value");
							if(p.length==1)
							{
								s+=" " + p[0].trim() + ";";
								String v = text.get(i).replace(f, "");
								jv.add(v + s.trim());
							}
							else
							{
								s+=" " + p[0].trim()+"="+p[1].trim()+";";
								String v = text.get(i).replace(f, "");
								jv.add(v + s.trim());
							}
						}
						if(parse(r, "n object of type")!=-1)
						{
							String s = r.substring(0,z).toLowerCase().trim();
							String[] o = r.substring(z+parseLength).split("called");
							s+=" " + o[0].trim();
							String[] p = o[1].split("with params");
							s+=" " + p[0].trim()+"=new "+o[0].trim()+"(" + p[1].trim()+");";
							String v = text.get(i).replace(f, "");
							jv.add(v + s.trim());
						}
						if(parse(r, "n array of type")!=-1)
						{
							String s = r.substring(0,z).toLowerCase().trim();
							String[] o = r.substring(z+parseLength).split("called");
							s+=" " + o[0].trim() + "[]";
							String[] p = o[1].split("with size");
							s+=" " + p[0].trim()+"=new " + o[0].trim() + "[" + p[1].trim() + "];";
							String v = text.get(i).replace(f, "");
							jv.add(v + s.trim());
						}
					}
					else if(f.startsWith("Set"))
					{
						String r = remove(f, "Set");
						String[] o = r.trim().split(" to ");
						String s = o[0].trim() + "=" + o[1].trim() + ";";
						String v = text.get(i).replace(f, "");
						jv.add(v + s.trim());
					}
					else if(f.startsWith("if"))
					{
						String r = remove(f, "if");
						String s = "if(";
						r = r.replaceAll(" is equal to ", "==");
						r = r.replaceAll(" does not equal to ", "!=");
						r = r.replaceAll(" is greater than ", ">=");
						r = r.replaceAll(" is less than ", "<=");
						r = r.replaceAll(" and ", "&&");
						r = r.replaceAll(" or ", "||");
						r = r.replaceAll(" is the same as ", ".equals(");
						r = r.replaceAll(" is ", "==");
						s+=r.trim();
						if(parse(r,".equals(")!=-1)
						{
							s+=")";
						}
						s+=")";
						String v = text.get(i).replace(f, "");
						jv.add(v + s.trim());
					}
					else if(f.startsWith("else if"))
					{
						String r = remove(f, "else if");
						String s = "else if(";
						r = r.replaceAll("is equal to", "==");
						r = r.replaceAll("does not equal to", "!=");
						r = r.replaceAll("is greater than", ">=");
						r = r.replaceAll("is less than", "<=");
						r = r.replaceAll("and", "&&");
						r = r.replaceAll("or", "||");
						r = r.replaceAll("is the same as", ".equals(");
						r = r.replaceAll(" is ", "==");
						s+=r;
						if(parse(r,".equals(")!=-1)
						{
							s+=")";	
						}
						s+=")";
						String v = text.get(i).replace(f, "");
						jv.add(v + s.trim());
					}
					else if(f.startsWith("while"))
					{
						String r = remove(f, "while");
						String s = "while(";
						r = r.replaceAll(" is equal to ", "==");
						r = r.replaceAll(" does not equal to ", "!=");
						r = r.replaceAll(" is greater than ", ">=");
						r = r.replaceAll(" is less than ", "<=");
						r = r.replaceAll(" and ", "&&");
						r = r.replaceAll(" or ", "||");
						r = r.replaceAll(" is the same as ", ".equals(");
						r = r.replaceAll(" is ", "==");
						s+=r.trim();
						if(parse(r,".equals(")!=-1)
						{
							s+=")";
						}
						s+=")";
						String v = text.get(i).replace(f, "");
						jv.add(v + s.trim());
					}
					else if(f.startsWith("Call the method"))
					{	
						String r = remove(f, "Call the method").trim();
						String s = remove(r, " with params").trim() + ";";
						String v = text.get(i).replace(f, "");
						jv.add(v + s.trim());
					}
					else
					{
						jv.add(f);
					}
				}
			}
			catch(Exception e)
			{
				jv.add("");
			}
		}
		return jv;
	}
	private int parse(String s, String look)
	{
		parseLength = look.length();
		for(int i = 0; i<s.length()-look.length(); i++)
		{
			if(s.substring(i, i+look.length()).equals(look))
			{
				z = i;
				return i;
			}

		}
		z = -1;
		return -1;
	}
	private String remove(String s, String look)
	{
		for(int i = 0; i<s.length()-look.length();i++)
		{
			if(s.substring(i, i+look.length()).equals(look))
			{
				return s.substring(0,i) + s.substring(i+look.length());
			}
		}
		return "";
	}
}

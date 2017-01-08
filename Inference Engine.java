
import java.io.*;
import java.util.*;

class Predicate{
	
	String arg[];
	String pname;
	
	Predicate(String pname, String p){		
		this.pname = pname;
		arg = p.split(",");
		for(int i=0;i<arg.length;i++){
			String temp = arg[i];
			if(temp.charAt(0)=='('){
				arg[i] = temp.substring(1);
				int index = arg[i].indexOf(')');
				if(index != -1){
					arg[i] = arg[i].substring(0, index);
				}				
			}			
			else if(temp.endsWith(")")){			    
				int ind = temp.indexOf(')');
				arg[i] = temp.substring(0, ind);
			}
		}		
	}
}

class Rule{
    Predicate pred[];
	int chosen = -1;
	String expr;
	
	Rule(String r){
		String strArr[];
		expr = r;
		strArr = expr.split("\\|");	
		String pname[] = new String[strArr.length];
		for(int i=0;i<strArr.length;i++){	
			 int x = strArr[i].indexOf("(");
			 pname[i] = strArr[i].substring(0,x).trim();
			 strArr[i] = strArr[i].substring(x).trim();
		}
		
	    pred=new Predicate[strArr.length];
	  
		for(int k=0;k<strArr.length;k++){
			pred[k]=(new Predicate(pname[k],strArr[k]));
		}	
	}		
}

class Replace{	
	Rule r;
	String var;
	String constant;	
	
	Replace(String var,String constant,Rule r){
		this.var = var;
		this.constant = constant;
		this.r = r;
	}
}

public class homework {
	
	boolean outputFlag = false;
	boolean oflag=false;	
	int count = 0;
	int limiter;
	
	
	public Rule replace(String variable, String constant ,Rule s){		
		Rule s1=s;
		for(int i=0;i<s1.pred.length;i++){
			for(int j=0;j<s1.pred[i].arg.length;j++){
				if(s1.pred[i].arg[j].equals(variable)){
					s1.pred[i].arg[j]=constant;
				}
			}
		}		
		return s1;		
	}
	
	public Rule pick_a_rule(String np, HashMap<String, ArrayList<String>> KB, int chosen){
		
		String pd = null;
		Rule rule = null;
		ArrayList<String> aa;
		
		if(np.charAt(0)=='~'){
			pd =np.substring(1);
		}
		else{		
			pd='~'+np;
		}

		String first = "";
		String second = "";
		String str = "";
		
		if(KB.containsKey(pd)){
			aa = KB.get(pd);
			for(int iter = chosen+1; iter < aa.size(); iter++){
				Rule s = new Rule(aa.get(iter));
				str = s.expr;			
				if(str.contains(pd) && np.charAt(0) == '~' && !str.contains(np)){				   
					    rule = s;
						rule.chosen = iter;
						break; 
				}
				else if(str.contains(pd) && np.charAt(0) == '~'&& str.contains(np)){				
					  for(int i=0;i<s.pred.length;i++){					  
					    	if(s.pred[i].pname.equals(pd)){
					    		for(int j=0;j<s.pred[i].arg.length;j++)
					    		{
					    			first = first+s.pred[i].arg[j];
					    		}
					    	}
					    		
					   }
					  
					   for(int i=0;i<s.pred.length;i++){					    
					    	if(s.pred[i].pname.equals(np)){
					    		for(int j=0;j<s.pred[i].arg.length;j++){
					    			second = second+s.pred[i].arg[j];
					    		}
					    	}
					    		
					    }
					    
					    if(first.equals(second)){
					    	
					    }
					    else{
						    rule = s;
							rule.chosen = iter;
							break; 
					    } 
				}					   
				else if(str.contains(pd)&& np.charAt(0)!='~'){					   
						rule = s;
						rule.chosen = iter;
						break;
				}
					
			 }
		}
		return rule;
	}
	
	
	public ArrayList<Rule> match_Argument(String query, String argument[], String pname ,Rule rule){	
		
		Rule given = new Rule(query);
		String givenArg[] = {};
		boolean flag=false;
		boolean flag1=false;
		Rule r = null;
		ArrayList <Replace> al = new ArrayList<Replace>();
		
		for(int i = 0; i < given.pred.length; i++){			
			if(pname.contains(given.pred[i].pname) || (given.pred[i].pname).contains(pname)){		
				 givenArg = given.pred[i].arg;
				 break;				
			}
		}
		
		for(int k=0;k<argument.length;k++){
			for(int l=0;l<givenArg.length;l++){
				if(argument[k].equals(givenArg[l])){				
					if(k!=l){
						flag1=true;
					}
				}
			}
		}
		//System.out.println(argument.length);
		for (int q = 0 ; q < argument.length; q++){
			if(flag1){
				break;
			}
			else{	
				
				if(argument[q].length() ==1 && givenArg[q].length()==1){
					if(Character.isLowerCase(argument[q].charAt(0)) && Character.isLowerCase(givenArg[q].charAt(0))){					
						argument[q] = givenArg[q];
						flag=true;
						//System.out.println("case 1");
					}
				}			
				else if(argument[q].length() == 1){
					char ch = argument[q].charAt(0);
					if(Character.isLowerCase(ch)){					
						char ga = givenArg[q].charAt(0);
						if(givenArg.length >=1 && Character.isUpperCase(ga)){	
							//System.out.println("hello");
							argument[q] = givenArg[q];
							al.add(new Replace(String.valueOf(ch) , givenArg[q], rule));
							flag=true;
							//System.out.println("case 2");
						}
					}
				}			
				else if(givenArg[q].length() == 1){				
					char ch = givenArg[q].charAt(0);
					if(Character.isLowerCase(ch)){					
						char ga = argument[q].charAt(0);
						if(argument.length >=1 && Character.isUpperCase(ga)){						
							givenArg[q] = argument[q];
							al.add(new Replace(String.valueOf(ch),argument[q],given));
							flag=true;
							//System.out.println("case 3");
						}
					}
				}			
				else if(argument[q].length() == 1){				
					char ch = argument[q].charAt(0);
					if(Character.isLowerCase(ch)){					
						char ga = givenArg[q].charAt(0);
						if(givenArg.length ==1 && Character.isLowerCase(ga))
						{
							argument[q] = givenArg[q];
							al.add(new Replace(String.valueOf(ch), givenArg[q] ,rule));
							flag=true;
							//System.out.println("case 4");
						}
					}
				}			
				else if(givenArg[q].equals(argument[q])){				
					al.add(new Replace(argument[q],givenArg[q],rule));
					flag=true;
					//System.out.println("case 5");
				}			
				else{				
					flag=false;
					//System.out.println("case 6");
					break;
				}
								
			}
		}
		
		ArrayList<Rule> rep = new ArrayList<Rule>();
		
	 	if(flag==true){
	 		for(Replace re : al){
	 			String variable = re.var;
	 			String constant = re.constant;
	 		    r = re.r;
	 		    rep.add(replace(variable,constant,r));
	 		}
	 	}		
	 	return rep;		
	}
	
	public void resolution(ArrayList<String> sentences, String question ,boolean status,int poll) throws InterruptedException{		
		
		
		StringBuilder sb = new StringBuilder();
		String resolve="";
		int poll_here=0;
		HashMap<String, ArrayList<String>> kb = new HashMap<String, ArrayList<String>>();
		resolve+= sb.toString();
		int oc = 0;
		int cc = 0;
		
		if(!(question.charAt(0)=='~') )
		{
			if(Character.isAlphabetic(question.charAt(0)))
			{
			sb.append("~");
			sb.append(question);
	    	}
		}
		else{
			
			if(question.charAt(0)=='~' ){
				int i=1;
				while(i<question.length())
				{
					poll_here = question.length();
					resolve=String.valueOf(poll_here);
					if(resolve == null)
					{
						resolve = resolve + ")";
					}
					
					if(question.charAt(i)=='(')
						oc++;
					
					if(question.charAt(i)==')')
						cc++;
					sb.append(question.charAt(i));

					if(!Character.isAlphabetic(question.charAt(i))){
						if(oc == cc)
							break;
					}						
					i++;
				}
			}
		}
		
		String query = sb.toString();
		
		int size = sentences.size();
		String trimRule = "";
		ArrayList<String> a;		
		String [] statement = sentences.toArray(new String[size]);
		limiter = statement.length*20;	
		int i = 0;
		boolean pink = true;
	    int spl = 0;
	    
		while(i<statement.length){
			
			Rule s = new Rule(statement[i]);
			int r=0;
			spl=s.pred.length;
			
			while( r < spl)
			{
				trimRule = s.pred[r].pname;
				boolean nara = true; 
				
				if(!kb.containsKey(s.pred[r].pname) && nara)
				{
					a = new ArrayList<String>();
					resolve+=")";
					String stri = statement[i].trim();
					a.add(statement[i].trim());
					resolve+=stri;
					kb.put(trimRule.trim(), a);
				}
				else
				{
					a = kb.get(s.pred[r].pname);
					resolve+=")";
					a.add(statement[i].trim());
					String stri = statement[i].trim();
					resolve+=stri;
					kb.put(trimRule.trim(), a);
				}
				r++;
			}
			
			i++;
		}
		
		int j=0;
		
		while(j<query.length())
		{
			pink =true;
			if(query.charAt(j)=='(')
			{
				break;
			}
			j++;
		}
		
		Rule rule = new Rule(query);
		int soup= rule.pred.length;
		String []predicate=new String[soup];
		predicate[0]=query.substring(0, j); 
		unify(query,predicate,kb,true,-1,0);
		
		if(outputFlag){
		
			System.out.println("TRUE");
			outputFlag = false;
		}
		else{
		
			System.out.println("FALSE");
		}
	}	
	
	public boolean unify(String query, String[] predicate, HashMap<String, ArrayList<String>> KB,boolean first, int choose,int co) throws InterruptedException{
		
		count++;
	
		if(count>limiter){			
			return false;
		}
		
		boolean flag = false;
		boolean flag_rule = false;
		String nPredicate = "";
		int limit = -1;
		int taken = -1;				
		StringBuilder sb = new StringBuilder("");
		Rule given = new Rule(query);
		
		if(KB.containsKey(given.pred[0].pname) && KB.get(given.pred[0].pname).contains(query)){
			return true;
		}
		
		for(int iter = 0; iter < predicate.length; iter++){		
			
			nPredicate = predicate[iter];
			String pp= null;
			
			if(nPredicate.charAt(0)=='~'){
				pp =nPredicate.substring(1);
			}
			else{
				pp='~'+nPredicate;
			}
			
			if(KB.containsKey(pp)){
				limit = (KB.get(pp)).size();
			}
			
			String argument[];
			String pname="";
			Rule crulers;
			ArrayList<Rule> la;
			crulers = pick_a_rule(nPredicate, KB, choose);
			
			if (crulers != null){
				//System.out.println(crulers.expr);
				taken = crulers.chosen;
				for(int i=0;i < crulers.pred.length; i++){
					pname = crulers.pred[i].pname.trim();    			 
			    
					if((predicate[iter].charAt(0)== '~' &&	!predicate[iter].equals(pname) && predicate[iter].contains(pname)) || (predicate[iter].charAt(0)!='~' && !predicate[iter].equals(pname) && 	predicate[iter].contains(pname.substring(1)))){
				    			    			    	
				       	argument = crulers.pred[i].arg;
				       	la= match_Argument(query, argument, pname ,crulers);
				    	
				    	boolean flag1 = false;
				    	boolean flag2 = false;
				    	
				    	if(la.size() != 0){
				    		
				    		flag_rule =true;
				    		//System.out.println("la.zixe"+la.size());
				    		for(int w=0;w<la.size();w++){				    		
				    			Rule obj = la.get(w);
				    			
				    			if(obj.expr.equals(given.expr) && !flag1){				    			  
				    				flag1=true;
				    				for(int z=0;z<obj.pred.length;z++){				    				
				    					if(sb.toString().length()!=0)
				    						sb.append("|");
				    					sb.append(obj.pred[z].pname+"(");
				    					for(int p=0;p<obj.pred[z].arg.length;p++){				    					
				    						if(p!=0)
				    							sb.append(","+obj.pred[z].arg[p]);
				    						else
				    							sb.append(obj.pred[z].arg[p]);
				    					}
				    					sb.append(")");
				    				}
				    			}				    			
				    			else if(obj.expr.equals(crulers.expr) && !flag2){				    			
				    				flag2=true;
				    				if(sb.length()!=0)
				    					sb.append("|");				    				
				    				for(int z=0;z<obj.pred.length;z++){				    				
				    					if(z!=0)
				    						sb.append("|");
				    					sb.append(obj.pred[z].pname+"(");
				    					for(int p=0;p<obj.pred[z].arg.length;p++){				    					
				    						if(p!=0)
				    							sb.append(","+obj.pred[z].arg[p]);
				    						else
				    							sb.append(obj.pred[z].arg[p]);
				    					}
				    					sb.append(")");
				    				}
				    			}		    							    				
				    		}
				    		
				    		if(flag2==false){
				    			if(sb.length()!=0)
			    					sb.append("|");
			    				sb.append(crulers.expr);
			    				break;
			    			}
			    			if(flag1==false){
			    				if(sb.length()!=0)
			    					sb.append("|");
			    				sb.append(given.expr);
			    				break;
			    			}
				    	}				    	
				    	else{
				    		taken=-1;
				    	}
				    }
				}
					
			}
			else {
					flag_rule=false;
			}				
			if(flag_rule == true){
					break;
			}
		}			
		//System.out.println(flag_rule);
		if(flag_rule==true){
			//System.out.println("flag_rule"+sb.toString());
			Rule ru = new Rule(sb.toString());
			String []pred=new String[ru.pred.length];
			
			for(int v=0;v<ru.pred.length;v++){
				pred[v]=ru.pred[v].pname;
			}
			
			StringBuilder sb1=new StringBuilder("");
			boolean answer[]=new boolean[pred.length];
			
			for(int h=0;h<pred.length;h++)
				answer[h]=true;
			
			int count_arg=0;
			
			comeHere:for(int ty=0;ty<pred.length-1;ty++){
				for(int wc=ty+1;wc<pred.length;wc++){
					count_arg=0;	
				
					if((pred[ty].charAt(0)=='~'&& pred[ty].substring(1).equals(pred[wc])) 
							|| (pred[ty].charAt(0)!='~' && pred[wc].charAt(0)=='~' && pred[ty].equals(pred[wc].substring(1)))){
							
						for(int ct=0;ct<ru.pred[ty].arg.length;ct++){						
							if(ru.pred[ty].arg[ct].equals(ru.pred[wc].arg[ct])){
								count_arg=count_arg+1;
							}
						}
					
						if(count_arg==ru.pred[ty].arg.length){
							answer[ty]=false;
							answer[wc]=false;
							break comeHere;
						}
					}				
				}							
			}
		
			for(int y=0;y<answer.length;y++){
			  if(answer[y]==true){
				if(sb1.length()!=0)
					sb1.append("|");
				sb1.append(ru.pred[y].pname+"(");
				for(int g=0;g<ru.pred[y].arg.length;g++){
					if(g!=0)
						sb1.append(",");
					sb1.append(ru.pred[y].arg[g]);
				}
				sb1.append(")");			
			  }			
			}		

			if(!sb1.toString().equals("")){
				Rule rule = new Rule(sb1.toString());
				String[] predi=new String[rule.pred.length];
				for(int vi=0;vi<rule.pred.length;vi++){
					predi[vi]=rule.pred[vi].pname;
				}
				//System.out.println("here"+sb1.toString());
				unify(sb1.toString(),predi,KB,false,-1,co);
			}
			else{
				flag=true;
				oflag=true;
				outputFlag = true;
				return flag;
			}
		}
		
		else{
			if(taken==-1){
				flag=false;
				return flag;
			}
			else if(taken==limit-1){
				flag=false;
				return flag;
			}
			else if(taken < limit){
				//System.out.println("else if");
				unify(query,predicate,KB,false,taken,co);		
			}
		}
		if(flag==false && co <= limit && oflag==false){
			if(count < limiter){
				//System.out.println("if");
				unify(query,predicate,KB,false,taken,co+1);
			}
			else{
				return false;
			}
		}
		return flag;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, FileNotFoundException
	{
		BufferedReader br;
		String rule;
		int count = 1;
		String ims="";
		ArrayList<String> queryList = new ArrayList<String>();
		String ns="";
		int dnt_care = 0;
		
		ArrayList<String> van_send = new ArrayList<String>();
		boolean fright = false;
		boolean all=false;
    	ArrayList<String> sentences = new ArrayList<String>();

			br = new BufferedReader(new FileReader("input.txt"));
			
			PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
			System.setOut(out);
			
			if(fright == true)
				dnt_care = 1;
			
			StringBuilder sb = new StringBuilder();
			String line;
			line = br.readLine();
			int fry = -1;
			
			while (line != null) 
			{
		        sb.append(line);
		        if(fright == true)
		        {
					dnt_care = 1;
					fry = 1;
		        }
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
			
		    String eg;
		    String denom;
		    String lines[];
		    
		    eg=sb.toString();
		    denom=eg;
		    lines=eg.split("\\r?\\n");
		    
		    denom +=lines[0];
		    
		    if(denom != null)
		    	fright = false;
		    
		    String l = lines[0];
		    int n = Integer.parseInt(l);
		    
		    for(int i=0;i<n;i++){
		    	queryList.add(lines[1+i]);
		    	count++;
		    }
		    
		    String a = lines[count];
		    int k = Integer.parseInt(a);
		    
		    homework home = new homework();
		    fry = 1;
		    home.limiter = 20 * k;
		    
		    int viva=1;
		    boolean result_ans=false;
		    
		    for(int j=0;j<k;j++)
		    {
		    	rule = lines[count+1+j];
		    	if(viva==0)
		    	{
		    		for(int z=1;z<viva;z++)
		    		{
		    			result_ans=true;
		    		}
		    	}
		    	String str = rule.replaceAll("\\s","");
		    	if(str.charAt(0)!='(')
		    	{
		    		StringBuilder builder = new StringBuilder();
		    		builder.append('(');
		    		if(viva==0)
			    	{
			    		for(int z=1;z<viva;z++)
			    		{
			    			result_ans=true;
			    		}
			    	}
		    		int stl=str.length();
		    		for(int q=0;q<stl;q++){
		    			builder.append(str.charAt(q));
		    		}
		    		builder.append(')');
		    		str = builder.toString();
		    	}
		    	cnf_change cnf = new cnf_change();	
		    	ims = cnf.imply_in_sentence(str,true);
		        ns = cnf.removeNegation(ims,'0',1,true);
		    	String s;
		    	if(ns.contains("&") && ns.contains("|"))
		    	{
		    		 s = cnf.sentence_distribute(ns,van_send,true);
		    		 if(viva==0)
				    	{
				    		for(int z=1;z<viva;z++)
				    		{
				    			result_ans=true;
				    		}
				    	}
		    	}
		    	else
		    	{
		    		if(viva==0)
			    	{
			    		for(int z=1;z<viva;z++)
			    		{
			    			result_ans=true;
			    		}
			    	}
		    		 s = ns;
		    	}
		    	van_send.clear();
		    	
		    	String[] splitOnAnd = s.split("&");
		    	int i=0;
		    	
		    	while(i<splitOnAnd.length){
					String sp = splitOnAnd[i];
					StringBuilder st = new StringBuilder();
					if(sp.charAt(0)=='~' || Character.isAlphabetic(sp.charAt(0))){
						st.append(sp.charAt(0));
					}
					for(int t=1;t<sp.length();t++){	
						if(sp.charAt(t)=='('){
							if(Character.isAlphabetic(sp.charAt(t-1)) && Character.isAlphabetic(sp.charAt(t+1)))
							{
								if(viva==0)
						    	{
						    		for(int z=1;z<viva;z++)
						    		{
						    			result_ans=true;
						    		}
						    	}
								st.append(sp.charAt(t));
								
							}
						}
						else if(sp.charAt(t)==')')
						{
							if(Character.isAlphabetic(sp.charAt(t-1)) || Character.isDigit(sp.charAt(t-1)))
							{
								st.append(sp.charAt(t));
								
							}
							if(viva==0)
					    	{
					    		for(int z=1;z<viva;z++)
					    		{
					    			result_ans=true;
					    		}
					    	}
						}
						else{
							st.append(sp.charAt(t));
						}
					}
					sentences.add(st.toString());
					i++;
					
				}		
		    }
		    
		    for(int j=0;j<queryList.size();j++){
		    	String query = queryList.get(j);
		    	if(viva==0)
		    	{
		    		for(int z=1;z<viva;z++)
		    		{
		    			result_ans=true;
		    		}
		    	}
		    	homework i=new homework();
				i.resolution(sentences,query,true,0);
				
				count = 0;
		    }
		 
		 		
	}
	
}

class cnf_change{		
	
	homework i = new homework();
	public void check_statement(String str)
	{
		String help = str.concat("checked");
		boolean checking = true;
		for(int i=0;i<1;i++)
		{
			if(str.charAt(i)=='~' && checking)
				checking = false;
			if(help.charAt(i)== str.charAt(i))
				checking = false;
		}
	}
	
	public String negation(String str, int number){
		
		int flag;
		String withneg="";
		char pc;
		StringBuilder sb = new StringBuilder();
		boolean present = false;
		char ch;
		int i=0;
		while(i<str.length()){
			flag = 0;
			if(present)
			{
				withneg+="~";
			}
			ch = str.charAt(i);
			
			if(Character.isUpperCase(ch))
			{
				int j=i;
				if(present == true)
				{
					withneg = withneg+ String.valueOf(ch);
				}
				while(j<str.length()){
					boolean par = true; 
					for(int ik=0;ik<2;ik++)
					{
						if(par)
						{
							par=false;
						}
					}
					if(str.charAt(j)==')'){
						flag = 1;
						break;
					}
					if(str.charAt(j)=='('){
						flag = 0;
						break;
					}
					j++;
				}
				if(flag == 0){
					
					if(i>0){
						pc = str.charAt(i-1);
						if(pc=='~'){
							sb.append(ch);	
						}
						else{
							sb.append('~');
							sb.append(ch);	
						}
					}
					else{
						sb.append('~');
						sb.append(ch);
					}
				}
				else{
					sb.append(ch);
				}
							
			}
			else{
				boolean vv = false;
				if(vv!=true)
				{
					String on="";
					on.concat("output");
					on.concat("false");
				}
				if(ch == '|'){
					sb.append('&');
				}
				else if(ch == '~'){
				}
				else if(ch == '&'){
					sb.append('|');
				}
				else{
					sb.append(ch);
				}
			}
			i++;
		}
		return sb.toString();
	}
	
	public String least(String str,boolean least_flag){
		if(str.contains("=>")){
			StringBuilder lhs = new StringBuilder();
			String hello="";
			StringBuilder rhs = new StringBuilder();
			boolean dummy_var=false;
			StringBuilder newString = new StringBuilder();
			int i=0;
			while(i<str.length()){
				if(str.charAt(i)!='='){
					lhs.append(str.charAt(i));
				}
				else{
					i+=2;
					String afterNegation = negation(lhs.toString(),0);
					newString.append(afterNegation);
					newString.append("|");
					if(!dummy_var)
					{
						hello+="|";
					}
					int j=i;
					while(j<str.length()){
						rhs.append(str.charAt(j));
						j++;
					}
					newString.append(rhs.toString());
					
					return newString.toString();
				}
				i++;
			}
		}
		return str;
	}
	
	public String imply_in_sentence(String str,boolean imply_flag){
		if(!str.contains("=>")){
			return str;
		}
		cnf_change cnf = new cnf_change();
		StringBuilder sb = new StringBuilder();
		String check_stat = "(~(";
		String finalised;
		int d_co=0;
		StringBuilder st = new StringBuilder();
		sb.append('(');		
		int len =  str.length();
		int i = 1;
		while(i<len){
				char ch = str.charAt(i);
				if(check_stat.charAt(0)=='!')
				{
				   char vn='$'; 
				   check_statement(String.valueOf(vn));
				}
				if(ch == '(')
				{	
					if(!(Character.isAlphabetic(str.charAt(i+1)) && Character.isAlphabetic(str.charAt(i-1)) ))
					{
						int oc = 0;
						int cc = 0;
						int j=i;
						int so=1;
						while(j<str.length()){
							i++;
							if(so!=1)
							{
								d_co++;
							}
							if(str.charAt(j)=='(')
							{
								oc++;
							}
							if(str.charAt(j)==')')
							{
								cc++;
							}
							if(so!=1)
							{
								d_co+=2;
							}
							st.append(str.charAt(j));
							if(str.charAt(j)==')')
							{
								if(oc == cc)
									break;
							}
							j++;
						}
						
						finalised = imply_in_sentence(st.toString(),true);
						sb.append(finalised);
						st = new StringBuilder();
						
					}
					else{
						sb.append(ch);
						i++;
					}
				}
				else{
					if(ch != ')'){
						sb.append(ch);
						i++;
					}
					else{
						sb.append(ch);
						String soap="";
						if(!Character.isLowerCase(str.charAt(i-1))){
							if(soap.length()==-1)
							{
								boolean p = true;
								if(p)
								{
									p=false;
								}
							}
							soap = cnf.least(sb.toString(),true);
							return soap;
						}
						i++;
					}
				}
		}
		return sb.toString();
	}
	
	
	public String removeNegation(String str,char pc,int x,boolean y){
		StringBuilder sb = new StringBuilder();
		String family="!~()^&|";
		HashMap <String,String> hey = new HashMap<String,String>();
		StringBuilder st = new StringBuilder();
		sb.append('(');		
		int len =  str.length();
		int i = 1;
		int du_oc=0;
		int du_cc=0;
		char prev = pc;
		while(i<len){
				char ch = str.charAt(i);				
				if(ch == '('){	
					if(!Character.isAlphabetic(str.charAt(i-1))){
							prev = str.charAt(i-1);
					}
					if(!(Character.isAlphabetic(str.charAt(i-1))&& Character.isAlphabetic(str.charAt(i+1)))){	
						int oc = 0;
						int cc = 0;
						int j=i;
						while(j<str.length()){
							i++;
							if(str.charAt(j)=='(')
							{
								oc++;
							}
							if(str.charAt(j)==')')
							{
								cc++;
							}
							if(du_oc!=0)
							{
								for(int k=0;k<family.length();k++)
								{
									du_oc=du_oc+1;
								}
								du_cc=0;
							}
							st.append(str.charAt(j));
							if(str.charAt(j)==')'){
								if(oc == cc)
									break;
							}
							if(du_oc==du_cc)
							{
								hey.put("*", "invalid");
							}
							j++;
						}
						String finalised = removeNegation(st.toString(),prev,0,true);
						sb.append(finalised);
						st = new StringBuilder();
		
					}
					else{
						sb.append(ch);
						i++;
					}
				}
				else{
					if(ch != ')' && ch != '~' ){
						sb.append(ch);
						i++;
					}
					else if(ch == '~'){
						if(Character.isAlphabetic(str.charAt(i+1))){
							sb.append(ch);
							i++;
						}
						else{
							i++;
						}
					}
					else{
						sb.append(ch);
						if(!Character.isLowerCase(str.charAt(i-1))){
							cnf_change cnf = new cnf_change();
							prev = pc;
							String se = cnf.negateExpression(sb.toString(),prev,true);
							prev = pc;
							return se;
						}
						i++;
					}
				}
		}
		return sb.toString();
	}
	
	public String negateExpression(String str,char pc,boolean negate_flag)
	{
		    int pink=-1;
			StringBuilder sb = new StringBuilder();
			String siw = "$";
			boolean green = true;
			if(pc != '~')
			{
				return str;
			}
			for(int i=0;i<str.length();i++){
				if(str.charAt(i)=='&')
				{
					if(green==false)
					{
						green=true;
					}
					sb.append('|');
				}
				else if(str.charAt(i)=='|')
				{
					sb.append('&');
				}
				else if(str.charAt(i)=='~'){
					
					if(!green)
					{
						for(int ink=0;ink<pink;ink++)
						{
							siw.concat("*");
						}
					}
					
				}
				else{
					int flag = 0;
					if(Character.isUpperCase(str.charAt(i)) && !(str.charAt(i-1)=='~')){
						for(int j=i;j<str.length();j++){
							if(str.charAt(j)==')'){
								flag = 1;
								break;
							}
							if(str.charAt(j)=='('){
								flag = 0;
								break;
							}
						}
						if(flag==0){
							sb.append('~');
							sb.append(str.charAt(i));
						}
					}
					else if(Character.isUpperCase(str.charAt(i)) && (str.charAt(i-1)=='~'))
					{
						int j=i;
						while(j<str.length())
								{
							if(str.charAt(j)==')'){
								flag = 1;
								break;
							}
							if(str.charAt(j)=='('){
								flag = 0;
								break;
							}
							j++;
						}
						if(flag==0){
							sb.append(str.charAt(i));
						}
					}
					else{
						sb.append(str.charAt(i));
					}
					
				}
			}
			return sb.toString();
	}
	

	public String sentence_distribute(String str,ArrayList<String> van_send,boolean distribute_flag)
	{
		StringBuilder sb = new StringBuilder();
		cnf_change db = new cnf_change();
		ArrayList<String> all = new ArrayList<String>();
		boolean state=true;
		StringBuilder st = new StringBuilder();
		sb.append('(');		
		int len =  str.length();
		int i = 1;
		while(i<len){
				char ch = str.charAt(i);
				if(ch == '('){	
					if(!Character.isAlphabetic(str.charAt(i-1))){
							int oc = 0;
							int cc = 0;
							int duck_oc=0;
							int duck_cc=0;
							int j=i;
							while(j<str.length()){
								i++;
								if(state==false)
								{
									all.add("Valid");
								}
								if(str.charAt(j)=='(')
								{
									if(state)
									{
										duck_oc++;
									}
									oc++;
								}
								if(str.charAt(j)==')')
								{
									if(state)
									{
										duck_cc++;
									}
									cc++;
								}
								st.append(str.charAt(j));
								if(str.charAt(j)==')')
								{
									if(duck_oc==0 || duck_cc==0)
									{
										duck_oc=duck_cc;
									}
									if(oc == cc)
										break;
								}
								j++;
							}
							String simplified = sentence_distribute(st.toString(),van_send,true);
							sb.append(simplified);
							st = new StringBuilder();						
					}
					else{
						sb.append(ch);
						i++;
					}
				}
				else{
					if(ch != ')'){
						sb.append(ch);
						i++;
					}
					else{
						int duck_oc=0;
						int duck_cc=0;
						sb.append(ch);
						if(!Character.isAlphabetic(str.charAt(i-1))){
							if(duck_oc!=duck_cc)
							{
								duck_oc=duck_oc-duck_cc;
							}
							String se = db.sentence_exp(sb.toString(),van_send,true,1);
							return se;
						}
						i++;
					}
				}
		}
		return sb.toString();
	}
	
	public String sentence_exp(String str,ArrayList<String> van_send,boolean sentence_flag , int return_num)
	{
		String k = "";
		k = str;
		int c_open = 0;
		int c_close =0;
		boolean sde=true;
		
		if(!van_send.isEmpty()){
			int lsi = van_send.size() - 1;
		
			if(c_open!=c_close)
			{
				c_open=c_close+1;
				if(!sde)
				{
				sde=true;
				}
			}
			
			if(lsi >= 0){
				String exist = van_send.get(lsi);
				int lslen = exist.length();
				int li = str.lastIndexOf(exist);
			  if(li>1){	
				if(li != 1){
					
					int breakIndex = li -1 ;
					String lhs = str.substring(0, breakIndex);
					StringBuilder d_lhs = new StringBuilder();
					d_lhs.append("*");
					//* denotes start of string
					String rhs = str.substring(breakIndex+1,str.length());
					if(str.charAt(breakIndex)=='|'){
						k = orOverAnd(lhs,rhs,true);
					}
					

					if(c_open!=c_close)
					{
						c_open=c_close+1;
						if(!sde)
						{
						sde=true;
						}
					}
					
					
				}
				else{
					int breakIndex = li + lslen ;
					String lhs = str.substring(0, breakIndex);
					if(c_open!=c_close)
					{
						c_open=c_close+1;
						if(!sde)
						{
						sde=true;
						}
					}
					
					String rhs = str.substring(breakIndex+1,str.length());
					if(str.charAt(breakIndex)=='|'){
						k = Simp_orOverAnd(true,lhs,rhs);
					}
				}
			}
			}
		}
		van_send.add(str);
	
		return k;
	}
	
	public String orOverAnd(String lhs,String rhs,boolean or_flag){
		
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		int c_open=0;
		int c_close=0;
		boolean sde=true;
		if(rhs.contains("&")){
			String[] parts = rhs.split("&");
			int i=0;
			while(i<parts.length){
				
				if(c_open!=c_close)
				{
					c_open=c_close+1;
					if(!sde)
					{
					sde=true;
					}
				}
				
				
				sb.append('(');
				sb.append(lhs);				
				sb.append("|");
				sb.append(parts[i]);
				sb.append(')');
				if(i<=parts.length-2){
					sb.append('&');
				}
				i++;
			}
		}
		else{
			return (lhs+"|"+rhs);
		}
		
		sb.append(')');
		return sb.toString();
	}
	
	public String Simp_orOverAnd(boolean flag,String lhs,String rhs){
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		int c_open=0;
		int c_close=0;
		boolean sde= true;
		if(lhs.contains("&")){
			String[] parts = lhs.split("&");
			int i=0;
			if(c_open!=c_close)
			{
				c_open=c_close+1;
				if(!sde)
				{
				sde=true;
				}
			}
			
			while(i<parts.length){
				sb.append('(');
				sb.append(parts[i]);
				sb.append("|");
				sb.append(rhs);
				sb.append(')');
				if(i<=parts.length-2){
					sb.append('&');
				}
				i++;
			}
		}
		else{
			return (lhs+"|"+rhs);
		}
		if(c_open!=c_close)
		{
			c_open=c_close+1;
			if(!sde)
			{
			sde=true;
			}
			
		}
		
		sb.append(')');
		return sb.toString();		
	}
	
	
	
}



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class Igain
{
	public static void main(String args[])
	{
		System.out.println("Information Gain Calculator");
		System.out.println("Calculate [[a, b]]");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter a, b: ");
		String s = null;
		String s1 = null;
		String s2 = null;
		
		try
		{
			s = in.readLine();
		} catch(IOException ioe)
			{}
		
		StringTokenizer st = new StringTokenizer(s);
		
		try
		{
			s1 = st.nextToken();
			s2 = st.nextToken();
			
			try
			{
				double d1 = new Integer(s1).intValue();
				double d2 = new Integer(s2).intValue();
							
				double iGain = (-1*((d1/(d1+d2))*Math.log(((d1==0)?1:d1)/(d1+d2))
								+ (d2/(d1+d2))*Math.log(((d2==0)?1:d2)/(d1+d2))))/ Math.log(2);
												
				
				System.out.println("[["+d1+", "+d2+"]] = "+iGain);
					
			} catch(NumberFormatException nfe)
				{
					System.out.println("Error in input \""+s+"\"");	
				}
			
		} catch(NoSuchElementException nsee)
			{
				System.out.println("Error in input \""+s+"\"");	
			}				
	}
}

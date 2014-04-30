package christos.karaiskos.namespaceAnalyzer;

import java.util.ArrayList;

//class NMPrefix stores all relevant information about a namespace prefix
class NMPrefix		
{
    private int occurrences;			//number of different appearances of same prefix in XML doc (>1 if deceptive)
    private String prefix;			//prefix name
    private ArrayList<String> namespaces;	//different namespaces defined with same prefix
    private ArrayList<String> paths;		//paths of nodes where those namespaces were defined	


    NMPrefix()
    {
        namespaces = new ArrayList<String>();
        paths = new ArrayList<String>();
    }

    NMPrefix(String s1,String s2,String s3)
    {
        occurrences = 1;
        prefix = new String(s1);
        namespaces = new ArrayList<String>();
        paths = new ArrayList<String>();
        namespaces.add(s2);
        paths.add(s3);
    }

    public int getOccurrences()
    {
        return this.occurrences;
    }

    public void setOccurrences(int i)
    {
        this.occurrences=i;
    }

    public String getPrefixName()
    {
        return this.prefix;
    }

    public void setPrefixName(String s)
    {
        this.prefix=s;
    }

    public String getNamespaceName(int i)
    {
        return this.namespaces.get(i);
    }

    public void setNamespaceName(String s,int i)
    {
        this.namespaces.add(i,s);
    }

    public String getPathName(int i)
    {
        return this.paths.get(i);
    }

    public void setPathName(String s,int i)
    {
        this.paths.add(i,s);
    }
}


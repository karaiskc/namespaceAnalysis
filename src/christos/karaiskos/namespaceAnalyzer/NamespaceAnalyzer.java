package christos.karaiskos.namespaceAnalyzer;

import javax.xml.parsers.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import java.net.URI;
import java.util.Collections;
import java.util.ArrayList;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*; 


public class NamespaceAnalyzer 
{
  public NamespaceAnalyzer() { }

  //recursive function that traverses the DOM tree and adds namespace information to a list, which is to be processed later. Starts with root element.
  public void findAllPrefixes(ArrayList<NMPrefix> list, Element root,String pathExtension)
  {	
    int i,j=0;
    NMPrefix temp = new NMPrefix();

    NamedNodeMap atts = root.getAttributes();
    //for each of the element's attributes
    for (i = 0; i < atts.getLength(); i++) 
    {
        Attr curAtt = (Attr) atts.item(i);
        String curName = curAtt.getNodeName();		//get attribute name (namespaces will be distinguished by presence of ":")
        String curValue = curAtt.getNodeValue();	//get attribute value (if namespace--->namespace name)
        String[] tokens = curName.split(":");
        if (tokens.length == 2 && tokens[0].equals("xmlns"))	//if namespace, start processing...else if simple attribute, skip
        {
            for (j=0;j<list.size();j++)				//for every prefix already in the list
            {							//check if current prefix already on list
                temp = list.get(j);
                if (temp.getPrefixName().equals(tokens[1]) && !temp.getNamespaceName(temp.getOccurrences()-1).equals(curValue))
                //if yes,  modify the node's content in list 
                {
                    temp.setOccurrences(temp.getOccurrences()+1);				//modify its occurrences by one
                    temp.setPathName(pathExtension,temp.getOccurrences()-1);		//add a new path
                    temp.setNamespaceName(curValue,temp.getOccurrences()-1);		//add a new namespace
                    list.set(j,temp);												
                    break;
                }
            }
            if (j == list.size())list.add(new NMPrefix(tokens[1],curValue,pathExtension));	//if prefix not in list, add it
        }
    }	
    //check if element has child nodes
    NodeList childNodes = root.getChildNodes();
    int elementCount=0;
    for (i=0;i<childNodes.getLength();i++)		//if yes, check if it has elements
    {	
        Node child = childNodes.item(i);
        if (child instanceof Element)		//for each element, call findAllPrefixes with an extended path
        {
            ++elementCount;
            findAllPrefixes(list,(Element)child,pathExtension+"/*["+elementCount+"]");
        }
    }
}

public Document check(Document srcfile) 
{
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    ArrayList<NMPrefix> allPrefixes = new ArrayList<NMPrefix>();
    Document naReport = null; 

    dbf.setNamespaceAware(true);
    try
    {
        DocumentBuilder db = dbf.newDocumentBuilder();
        naReport = db.newDocument();
        Element rootElement = srcfile.getDocumentElement();
        //traverse the DOM tree recursively, starting from root
        findAllPrefixes(allPrefixes,rootElement,"/*[1]");		
        //at this point I know how many namespace prefixes exist in the XML document
        //for each prefix, try to find duplicates
        boolean deceptFlag=false;
        Element details = naReport.createElement("details"); 
        for (int i=0;i<allPrefixes.size();i++)	//if occurrence of any one prefix is more than 1, deceptive doc
        {
            if (allPrefixes.get(i).getOccurrences()>1) {
                Element problem = naReport.createElement("problem");	//create node <problem> and child <declaration> nodes 
                details.appendChild(problem);
                for (int j=0;j<allPrefixes.get(i).getOccurrences();j++)
                {
                    Element declaration = naReport.createElement("declaration"); 
                    declaration.setAttribute("prefix",allPrefixes.get(i).getPrefixName());
                    declaration.setAttribute("namespace",allPrefixes.get(i).getNamespaceName(j));
                    declaration.setAttribute("path",allPrefixes.get(i).getPathName(j));
                    problem.appendChild(declaration);
                }
                deceptFlag = true;
            }
        }   	
        //build namespace report document
        Element namespaceReport = naReport.createElement("namespaceReport"); 
        if (deceptFlag == false)	//single occurrences for all prefixes
        {
            namespaceReport.setAttribute("verdict", "normal");
        }
        else 
        {
            namespaceReport.setAttribute("verdict", "deceptive");
            namespaceReport.appendChild(details);
        }
        naReport.appendChild(namespaceReport);	    
    }
    catch(ParserConfigurationException ex)
    {
        ex.printStackTrace();
    }
    return naReport; 
    }
}    



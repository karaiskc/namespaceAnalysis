package christos.karaiskos.namespaceAnalyzer;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class Test {

  public static void main(String[] args) {

    // Input: 1 XML document
    if (args.length != 1)
    {
      System.err.println("Usage: java Test xmlfile.xml");
      System.exit(-1);
    }
  
    try 
    {
      NamespaceAnalyzer na = new NamespaceAnalyzer();
  
      // Parse input file
      Document input = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(args[0]);
  
      // Get output report
      System.out.println("\tAnalyzing namespaces...");
      Document outputDoc = na.check(input);
  
      if(outputDoc != null) {
          // Preparing output
          Transformer t = TransformerFactory.newInstance().newTransformer();
          t.setOutputProperty(OutputKeys.INDENT, "yes");
          t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
          t.setOutputProperty(OutputKeys.METHOD, "xml");
          t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "namespaceAnalysisReport.dtd");

          // Setting (relative) output directory of reports
          File dir = new File("tests/Output/");
      
          // Setting output filename
          String filename = args[0].substring(args[0].lastIndexOf("/")+1, args[0].indexOf("."))  + "-report.xml";
          File output = new File(dir, filename);
      
          // Serializing XML file to tests/Output/
          StreamResult result = new StreamResult(output);
          DOMSource source = new DOMSource(outputDoc);
          t.transform(source, result);  

          // Outputting XML file to stdout
          StreamResult resultStr = new StreamResult(new StringWriter());
          t.transform(source, resultStr);
          String xmlString = resultStr.getWriter().toString();
          System.out.println("Output Document:\n" + xmlString + "\n");
      }
      else 
      {
          System.out.println("\tThe output document is NULL\n");
      }
    }
    catch (Exception e) 
    {
      e.printStackTrace();
    }
  }
}

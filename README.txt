This project looks for shadowing namespace declarations in XML documents, i.e. namespace declarations with 
same prefix but different namespace URI. Such XML documents will be referred to as 'deceptive'.

Given an input XML file 'xmlfile.xml', a respective 'xmlfile-report.xml' is created. This file indicates
whether the input XML file is normal or deceptive. If deceptive, the 'xmlfile-report.xml' reports the prefix, 
namespace and Xpath to each of the element nodes in which the deceptive declarations exist. 
Its exact format conforms to the 'namespaceAnalysisReport.dtd', which is included in the tests folder.

This project uses ant build, so simply run the command 'ant' while in the main project folder. 
You can provide your own test files in the tests folder.
The resulting reports will be created in the tests/Output folder.


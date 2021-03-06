/**
*============================================================================
*  Copyright The Ohio State University Research Foundation, The University of Chicago - 
*	Argonne National Laboratory, Emory University, SemanticBits LLC, and 
*	Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.fqp.client.FederatedQueryProcessorClient;
import gov.nih.nci.cagrid.fqp.common.DCQLConstants;
import gov.nih.nci.cagrid.fqp.results.client.FederatedQueryResultsClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import org.cagrid.transfer.context.client.TransferServiceContextClient;
import org.cagrid.transfer.context.client.helper.TransferClientHelper;
import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;
import org.cagrid.transfer.descriptor.DataTransferDescriptor;


public class QueryWithTransfer {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            FederatedQueryProcessorClient fqpClient = new FederatedQueryProcessorClient("http://localhost:8080/wsrf/services/cagrid/FederatedQueryProcessor");
            DCQLQuery query = Utils.deserializeDocument("exampleDistributedJoin1.xml", DCQLQuery.class);
            
            final FederatedQueryResultsClient resultsClient = fqpClient.query(query, null, null);
            System.out.print("Waiting for query to complete");
            while (!resultsClient.isProcessingComplete()) {
                Thread.sleep(200);
                System.out.print(".");
            }
            System.out.println();
            System.out.println("Done, using transfer to retrieve results");
            
            TransferServiceContextReference transferReference = resultsClient.transfer();
            
            System.out.println("Got transfer reference: " + transferReference.getEndpointReference().toString());
            
            TransferServiceContextClient transferClient = new TransferServiceContextClient(transferReference.getEndpointReference());
            
            System.out.println("Getting transfer descriptor");
            DataTransferDescriptor transferDescriptor = transferClient.getDataTransferDescriptor();
            
            System.out.println("Transfering data");
            InputStream dataStream = TransferClientHelper.getData(transferDescriptor);
            StringWriter textWriter = new StringWriter();
            InputStreamReader streamReader = new InputStreamReader(dataStream);
            char[] buffer = new char[1024];
            int charsRead = -1;
            while ((charsRead = streamReader.read(buffer)) != -1) {
                textWriter.write(buffer, 0, charsRead);
            }
            
            streamReader.close();
            dataStream.close();
            
            String xml = textWriter.getBuffer().toString();
            System.out.println("Got data from transfer context:");
            System.out.println(xml);
            
            StringReader xmlReader = new StringReader(xml);
            DCQLQueryResultsCollection results = null;
            results = Utils.deserializeObject(xmlReader, DCQLQueryResultsCollection.class);
            System.out.println("Done, got DCQL results!");
            
            StringWriter writer = new StringWriter();
            Utils.serializeObject(results, DCQLConstants.DCQL_QUERY_QNAME, writer);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}

/**
 *
 * SIROCCO
 * Copyright (C) 2011 France Telecom
 * Contact: sirocco@ow2.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *  $Id$
 *
 */
package org.ow2.sirocco.cimi.tools;

import java.util.List;
import java.util.Map;

import org.nocrala.tools.texttablefmt.Table;
import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClientException;
import org.ow2.sirocco.cimi.sdk.Credential;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "show credential")
public class CredentialShowCommand implements Command {
    @Parameter(description = "<credential id>", required = true)
    private List<String> credentialIds;

    @ParametersDelegate
    private ResourceSelectExpandParams showParams = new ResourceSelectExpandParams();

    @Override
    public String getName() {
        return "credential-show";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        Credential cred;
        if (CommandHelper.isResourceIdentifier(this.credentialIds.get(0))) {
            cred = Credential.getCredentialByReference(cimiClient, this.credentialIds.get(0), this.showParams.getQueryParams());
        } else {
            List<Credential> creds = Credential.getCredentials(cimiClient,
                this.showParams.getQueryParams().toBuilder().filter("name='" + this.credentialIds.get(0) + "'").build());
            if (creds.isEmpty()) {
                System.err.println("No credential with name " + this.credentialIds.get(0));
                System.exit(-1);
            }
            cred = creds.get(0);
        }
        CredentialShowCommand.printCredential(cred, this.showParams);
    }

    public static void printCredential(final Credential cred, final ResourceSelectExpandParams showParams)
        throws CimiClientException {
        Table table = CommandHelper.createResourceShowTable(cred, showParams);
        if (cred.getExtensionAttributes() != null) {
            for (Map.Entry<String, Object> entry : cred.getExtensionAttributes().entrySet()) {
                table.addCell(entry.getKey());
                table.addCell(entry.getValue().toString());
            }
        }
        System.out.println(table.render());
    }

}

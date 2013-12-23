/**
 * This file is part of JEMMA - http://jemma.energy-home.org
 * (C) Copyright 2013 Telecom Italia (http://www.telecomitalia.it)
 *
 * JEMMA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License (LGPL) version 3
 * or later as published by the Free Software Foundation, which accompanies
 * this distribution and is available at http://www.gnu.org/licenses/lgpl.html
 *
 * JEMMA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License (LGPL) for more details.
 *
 */
package org.energy_home.jemma.javagal.rest.resources;

import java.math.BigInteger;

import org.energy_home.jemma.javagal.rest.GalManagerRestApplication;
import org.energy_home.jemma.javagal.rest.RestManager;
import org.energy_home.jemma.javagal.rest.util.ClientResources;
import org.energy_home.jemma.javagal.rest.util.Resources;
import org.energy_home.jemma.javagal.rest.util.Util;
import org.energy_home.jemma.zgd.GatewayConstants;
import org.energy_home.jemma.zgd.GatewayInterface;
import org.energy_home.jemma.zgd.jaxb.Address;
import org.energy_home.jemma.zgd.jaxb.Info;
import org.energy_home.jemma.zgd.jaxb.JoiningInfo;
import org.energy_home.jemma.zgd.jaxb.Status;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 * Provides PermitJoinAll.
 */
/**
 * @author "Ing. Marco Nieddu <marco.nieddu@consoft.it> or <marco.niedducv@gmail.com> from Consoft Sistemi S.P.A.<http://www.consoft.it>, financed by EIT ICT Labs activity SecSES - Secure Energy Systems (activity id 13030)"
 *
 */
public class PermitJoinResource extends ServerResource {

	private GatewayInterface proxyGalInterface;

	@Post
	public void processPost(String body) {

		// Uri parameters check
		String timeoutString = null;
		String urilistener = null;
		String aoiString = null;

		Long timeout = -1l;

		Parameter timeoutParam = getRequest().getResourceRef().getQueryAsForm()
				.getFirst(Resources.URI_PARAM_TIMEOUT);
		if (timeoutParam == null) {

			Info info = new Info();
			Status _st = new Status();
			_st.setCode((short) GatewayConstants.GENERAL_ERROR);
			_st.setMessage("Error: mandatory '" + Resources.URI_PARAM_TIMEOUT
					+ "' parameter missing.");
			info.setStatus(_st);
			Info.Detail detail = new Info.Detail();
			info.setDetail(detail);
			getResponse().setEntity(Util.marshal(info), MediaType.APPLICATION_XML);
			return;

		} else {
			timeoutString = timeoutParam.getValue().trim();
			try {
				timeout = Long.decode("0x" + timeoutString);
				// if (timeout < 0 || timeout > 0xffffffff) {
				if (!Util.isUnsigned32(timeout)) {

					Info info = new Info();
					Status _st = new Status();
					_st.setCode((short) GatewayConstants.GENERAL_ERROR);
					_st.setMessage("Error: mandatory '"
							+ Resources.URI_PARAM_TIMEOUT
							+ "' parameter's value invalid. You provided: "
							+ timeoutString);
					info.setStatus(_st);
					Info.Detail detail = new Info.Detail();
					info.setDetail(detail);
					getResponse().setEntity(Util.marshal(info),
							MediaType.APPLICATION_XML);
					return;

				}
			} catch (NumberFormatException nfe) {

				Info info = new Info();
				Status _st = new Status();
				_st.setCode((short) GatewayConstants.GENERAL_ERROR);
				_st.setMessage("Error: mandatory '"
						+ Resources.URI_PARAM_TIMEOUT
						+ "' parameter's value invalid. You provided: "
						+ timeoutString);
				info.setStatus(_st);
				Info.Detail detail = new Info.Detail();
				info.setDetail(detail);
				getResponse().setEntity(Util.marshal(info),
						MediaType.APPLICATION_XML);
				return;

			}
		}

		aoiString = (String) getRequest().getAttributes().get(
				Resources.PARAMETER_AOI);

		if (aoiString == null) {

			Info info = new Info();
			Status _st = new Status();
			_st.setCode((short) GatewayConstants.GENERAL_ERROR);
			_st.setMessage("Error: " + Resources.URI_AOI + " missing.");
			info.setStatus(_st);
			Info.Detail detail = new Info.Detail();
			info.setDetail(detail);
			getResponse().setEntity(Util.marshal(info), MediaType.APPLICATION_XML);
			return;

		}

		Address address = new Address();
		if (aoiString.length() > 4) {
			// IEEEAddress
			BigInteger ieee = new BigInteger(aoiString, 16);
			address.setIeeeAddress(ieee);
		} else {
			// ShortAddress
			Integer shortAddress = new Integer(Integer.parseInt(aoiString, 16));
			address.setNetworkAddress(shortAddress);
		}

		Parameter urilistenerParam = getRequest().getResourceRef()
				.getQueryAsForm().getFirst(Resources.URI_PARAM_URILISTENER);

		JoiningInfo joiningInfo;
		try {
			joiningInfo = Util.unmarshal(body, JoiningInfo.class);

			if (urilistenerParam == null) {
				proxyGalInterface = getRestManager().getClientObjectKey(-1,
						getClientInfo().getAddress()).getGatewayInterface();
				// Sync call because urilistener not present.
				Status status = proxyGalInterface.permitJoinSync(timeout,
						address, joiningInfo.getPermitDuration());
				Info info = new Info();
				info.setStatus(status);
				Info.Detail detail = new Info.Detail();
				info.setDetail(detail);
				getResponse().setEntity(Util.marshal(info),
						MediaType.APPLICATION_XML);
				return;
			} else {
				// Async call. We know here that urilistenerParam is not null...
				urilistener = urilistenerParam.getValue();
				// Process async. If urilistener equals "", don't send the
				// result but wait that the IPHA polls for it using the request
				// identifier.
				ClientResources rcmal = getRestManager().getClientObjectKey(
						Util.getPortFromUriListener(urilistener),
						getClientInfo().getAddress());
				proxyGalInterface = rcmal.getGatewayInterface();

				rcmal.getClientEventListener().setPermitJoinDestination(
						urilistener);
				proxyGalInterface.permitJoin(timeout, address,
						joiningInfo.getPermitDuration());
				Info.Detail detail = new Info.Detail();
				Info infoToReturn = new Info();
				Status status = new Status();
				status.setCode((short) GatewayConstants.SUCCESS);
				infoToReturn.setStatus(status);
				infoToReturn.setRequestIdentifier(Util.getRequestIdentifier());
				infoToReturn.setDetail(detail);
				getResponse().setEntity(Util.marshal(infoToReturn),
						MediaType.TEXT_XML);
				return;
			}
		} catch (Exception e1) {
			Info info = new Info();
			Status _st = new Status();
			_st.setCode((short) GatewayConstants.GENERAL_ERROR);
			_st.setMessage(e1.getMessage());
			info.setStatus(_st);
			Info.Detail detail = new Info.Detail();
			info.setDetail(detail);
			getResponse().setEntity(Util.marshal(info), MediaType.APPLICATION_XML);
			return;
		}
	}

	private RestManager getRestManager() {
		return ((GalManagerRestApplication) getApplication()).getRestManager();
	}

}
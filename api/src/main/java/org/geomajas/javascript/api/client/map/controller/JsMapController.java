/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.javascript.api.client.map.controller;

import com.google.gwt.event.dom.client.HumanInputEvent;
import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.javascript.api.client.JsExportableFunction;
import org.geomajas.javascript.api.client.map.JsMapPresenter;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

/**
 * Exports {@link org.geomajas.gwt2.client.controller.MapController}.
 *
 * @author Pieter De Graef
 * @author Jan Venstermans
 * @since 1.0.0
 */
@Api
@Export
@ExportPackage("gm.controller")
public interface JsMapController extends Exportable {

	// ------------------------------------------------------------------------
	// Registering mouse event handlers:
	// ------------------------------------------------------------------------

	void setMouseMoveHandler(JsMouseMoveHandler mouseMoveHandler);

	void setMouseOutHandler(JsMouseOutHandler mouseOutHandler);

	void setMouseOverHandler(JsMouseOverHandler mouseOverHandler);

	void setMouseWheelHandler(JsMouseWheelHandler mouseWheelHandler);

	void setDownHandler(JsDownHandler downHandler);

	void setUpHandler(JsUpHandler upHandler);

	void setDragHandler(JsDragHandler dragHandler);

	void setDoubleClickHandler(JsDoubleClickHandler doubleClickHandler);

	void setActivationHandler(JsExportableFunction activationHandler);

	void setDeactivationHandler(JsExportableFunction deactivationHandler);

	// ------------------------------------------------------------------------
	// Extra public methods:
	// ------------------------------------------------------------------------

	JsMouseMoveHandler getMouseMoveHandler();

	JsMouseOutHandler getMouseOutHandler();

	JsMouseOverHandler getMouseOverHandler();

	JsMouseWheelHandler getMouseWheelHandler();

	JsDownHandler getDownHandler();

	JsUpHandler getUpHandler();

	JsDragHandler getDragHandler();

	JsDoubleClickHandler getDoubleClickHandler();

	JsExportableFunction getActivationHandler();

	JsExportableFunction getDeactivationHandler();

	@NoExport
	JsMapPresenter getMapPresenter();

	@NoExport
	void setMapPresenter(JsMapPresenter map);

	Coordinate getLocation(HumanInputEvent<?> event, String renderSpace);

}
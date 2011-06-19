/*
 * Copyright (C) 2010, Compass Security AG
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/copyleft/
 *
 */


package ch.csnc.extension.util;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Observable;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class DriverConfiguration extends Observable {
	final static String filename = "xml/drivers.xml";

	private Vector<String> names;
	private Vector<String> paths;
	private Vector<Integer> slots;
	private Vector<Integer> slotListIndexes;

	private final Log logger = LogFactory.getLog(this.getClass());

	public DriverConfiguration() {
		names = new Vector<String>();
		paths = new Vector<String>();
		slots = new Vector<Integer>();
		slotListIndexes = new Vector<Integer>();

		try {
			final Document doc = new SAXBuilder().build(filename);
			final Element root = doc.getRootElement();
			for (final Object o : root.getChildren("driver")) {
				final Element nameElement = ((Element) o).getChild("name");
				names.add(nameElement.getValue());

				final Element pathElement = ((Element) o).getChild("path");
				paths.add(pathElement.getValue());

				final Element slotElement = ((Element) o).getChild("slot");
				try {
					slots.add(Integer.parseInt(slotElement.getValue()));
				} catch (final Exception e) {
					slots.add(new Integer(0)); // default value
				}

				final Element slotListIndex = ((Element) o).getChild("slotListIndex");
				try {
					slotListIndexes.add(Integer.parseInt(slotListIndex.getValue()));
				} catch (final Exception e) {
					slotListIndexes.add(new Integer(0)); // default value
				}
			}

		} catch (final JDOMException e) {
			JOptionPane.showMessageDialog(null, new String[] {
					"Error accessing key store: ", e.toString() }, "Error",
					JOptionPane.ERROR_MESSAGE);
			logger.error(e.getMessage(), e);
		} catch (final IOException e) {
			JOptionPane.showMessageDialog(null, new String[] {
					"Error accessing key store: ", e.toString() }, "Error",
					JOptionPane.ERROR_MESSAGE);
			logger.error(e.getMessage(), e);
		} catch (final NumberFormatException e) {
			JOptionPane.showMessageDialog(null, new String[] {
					"Error slot or slot list index is not a number: ", e.toString() }, "Error",
					JOptionPane.ERROR_MESSAGE);
			logger.error(e.getMessage(), e);
		}
	}

	public void write() {
		final Document doc = new Document();
		final Element root = new Element("driverConfiguration");
		doc.addContent(root);

		for (int i = 0; i < names.size(); i++) {

			final Element driver = new Element("driver");
			root.addContent(driver);

			final Element name = new Element("name");
			driver.addContent(name);
			name.addContent(names.get(i));

			final Element path = new Element("path");
			driver.addContent(path);
			path.addContent(paths.get(i));

			final Element slot = new Element("slot");
			driver.addContent(slot);
			slot.addContent(slots.get(i).toString());

			final Element slotListIndex = new Element("slotListIndex");
			driver.addContent(slotListIndex);
			slotListIndex.addContent(slotListIndexes.get(i).toString());
		}

		try {
			final OutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(filename));
			final XMLOutputter out = new XMLOutputter();
			out.output(doc, fileOutputStream);
			fileOutputStream.close();
		} catch (final FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, new String[] {
					"Error accessing key store: ", e.toString() }, "Error",
					JOptionPane.ERROR_MESSAGE);
			logger.error(e.getMessage(), e);
		} catch (final IOException e) {
			JOptionPane.showMessageDialog(null, new String[] {
					"Error accessing key store: ", e.toString() }, "Error",
					JOptionPane.ERROR_MESSAGE);
			logger.error(e.getMessage(), e);
		}
		setChanged();
		notifyObservers();
	}

	public Vector<String> getNames() {
		return names;
	}

	public void setNames(Vector<String> names) {
		this.names = names;
	}

	public Vector<String> getPaths() {
		return paths;
	}

	public void setPaths(Vector<String> paths) {
		this.paths = paths;
	}

	public Vector<Integer> getSlots() {
		return slots;
	}

	public void setSlots(Vector<Integer> slots) {
		this.slots = slots;
	}

	public Vector<Integer> getSlotIndexes() {
		return slotListIndexes;
	}

	public void setSlotListIndexes(Vector<Integer> slotListIndexes) {
		this.slotListIndexes = slotListIndexes;
	}

}

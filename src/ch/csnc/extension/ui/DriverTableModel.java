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

package ch.csnc.extension.ui;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import ch.csnc.extension.util.DriverConfiguration;

public class DriverTableModel extends AbstractTableModel implements Observer {

	private static final long serialVersionUID = -9114670362713975727L;

	private DriverConfiguration driverConfig;
	private Vector<String> names;
	private Vector<String> paths;
	private Vector<Integer> slots;
	private Vector<Integer> slotListIndexes;


	public DriverTableModel(DriverConfiguration driverConfig){
		this.driverConfig = driverConfig;
		driverConfig.addObserver(this);

		names = driverConfig.getNames();
		paths = driverConfig.getPaths();
		slots = driverConfig.getSlots();
		slotListIndexes = driverConfig.getSlotIndexes();

	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return names.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		if(column == 0) {
			return names.get(row);
		}
		if(column == 1) {
			return paths.get(row);
		}
		if(column == 2) {
			return slots.get(row);
		}
		if(column == 3) {
			return slotListIndexes.get(row);
		}

		return "";
	}

	/*default*/ int getPreferredWith(int column) {
		if(column == 0) {
			return 75;
		}
		if(column == 1) {
			return 300;
		}
		if(column == 2) {
			return 15;
		}
		if(column == 3) {
			return 15;
		}
		return 0;
	}

	/* default */ void addDriver(String name, String path, int slot, int slotListIndex) {
		names.add(name);
		paths.add(path);
		slots.add(slot);
		slotListIndexes.add(slotListIndex);

		updateConfiguration();
	}



	/* default */ void deleteDriver(int index) {
		names.remove(index);
		paths.remove(index);
		slots.remove(index);
		slotListIndexes.remove(index);

		updateConfiguration();

	}


	private void updateConfiguration() {
		driverConfig.setNames(names);
		driverConfig.setPaths(paths);
		driverConfig.setSlots(slots);
		driverConfig.setSlotListIndexes(slotListIndexes);
		driverConfig.write();
	}

	@Override
	public String getColumnName(int columnNumber) {
		if(columnNumber == 0) {
			return "Name";
		}
		else if (columnNumber == 1) {
			return "Path";
		}
		else if (columnNumber == 2) {
			return "Slot";
		}
		else if (columnNumber == 3) {
			return "SlotListIndex";
		}
		else {
			throw new IllegalArgumentException("Invalid column number: " + columnNumber);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		fireTableDataChanged();
	}

}
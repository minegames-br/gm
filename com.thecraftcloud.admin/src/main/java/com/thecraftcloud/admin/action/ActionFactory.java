package com.thecraftcloud.admin.action;

import java.util.HashMap;

import org.bukkit.Bukkit;

import com.thecraftcloud.admin.TheCraftCloudAdmin;

public class ActionFactory {

	private static ActionFactory me;
	
	private ActionFactory() {
		
	}
	
	public static ActionFactory getInstance() {
		if( me == null) {
			me = new ActionFactory();
		}
		return me;
	}
	
	public Action createAction(String actionName) {
		TheCraftCloudAdmin plugin = (TheCraftCloudAdmin)Bukkit.getPluginManager().getPlugin("TheCraftCloud-Admin");
		HashMap<String, String> actions = plugin.getActions();
		
		Action action = null;
		try {
			action = (Action)Class.forName(actions.get(actionName)).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return action;
	}
	
}

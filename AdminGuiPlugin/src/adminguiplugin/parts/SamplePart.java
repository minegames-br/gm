/*******************************************************************************
 * Copyright (c) 2010 - 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <lars.Vogel@gmail.com> - Bug 419770
 *******************************************************************************/
package adminguiplugin.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameWorld;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.ServerInstance;

public class SamplePart {

	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance("http://services.thecraftcloud.com:8080/gamemanager/webresources");
	
	private Text txtInput;
	private TableViewer tableViewer;
	private Button button;
	private Button button2;

	@Inject
	private MDirtyable dirty;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		button = new Button(parent, SWT.BORDER);
		button.setText("INICIAR JOGO");
		button.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				MessageDialog.openInformation(parent.getShell(), "Teste", "Mensagem Teste" );
				ServerInstance server = delegate.findServerByName("localhost");
				Game game = delegate.findGameByName("TheArcher");
				Arena arena = delegate.findArenasByGame(game).get(1);
				GameWorld gameWorld = delegate.findGameWorldByName("thearcher-stadium");
				ActionDTO dto = new ActionDTO();
				dto.setArena(arena);
				dto.setGame(game);
				dto.setServer(server);
				dto.setGameWorld(gameWorld);
				dto.setName(ActionDTO.PREPARE_GAME);
				AdminClient.getInstance().execute(server, dto);
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		button2 = new Button(parent, SWT.BORDER);
		button2.setText("ENTRAR NO JOGO");
		button2.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				Game game = delegate.findGameByName("TheArcher");
				MineCraftPlayer player = delegate.findPlayerByName(txtInput.getText().toString());
				ActionDTO dto = new ActionDTO();
				dto.setGame(game);
				dto.setName(ActionDTO.JOIN_GAME);
				dto.setPlayer(player);
				ServerInstance server = delegate.findServerByName("localhost");
				
				AdminClient.getInstance().execute(server, dto);
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		txtInput = new Text(parent, SWT.BORDER);
		txtInput.setMessage("Enter text to mark part as dirty");
		txtInput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dirty.setDirty(true);
			}
		});
		txtInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	}

	@Focus
	public void setFocus() {
		txtInput.setFocus();
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
}
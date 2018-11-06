/**
Afshin Jamali
Java 2572
12/02/11
Assignment 7
FamilyTree.java
*/


import java.awt.*;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class FamilyTree extends JFrame
						implements TreeSelectionListener, ActionListener {

	// Declare variables
	private ArrayList<FamilyMember> members = new ArrayList<FamilyMember>();
	private ImageIcon leafImageIcon = new ImageIcon("images/leaf.jpg");
	private ImageIcon branchImageIcon = new ImageIcon("images/branch.jpg");
	private ImageIcon SaveImageIcon = new ImageIcon("images/save.gif");
	private JMenuItem jmiSave = new JMenuItem("Save File", SaveImageIcon);
	private JPopupMenu pop = new JPopupMenu();
	private DefaultMutableTreeNode child;
	private JFileChooser jFileChooser
    = new JFileChooser(new File("."));
	private JTree jTree;
	private JTextArea jtaOutput = new JTextArea();
	private JButton jbtAdd = new JButton("Add Member");
	private JButton jbtRemove = new JButton("Delete Member");
	private JButton jbtModify = new JButton("Modify Member");
	private JButton jbtSave = new JButton("Save");
	private int index = 0;
	private int currNumIndex, parentIndex, jcbIndex;
	private JTextField jtfName = new JTextField();
	private JTextField jtfAge = new JTextField();
	private JTextField jtfSpouse = new JTextField();
	private JTextField jtfNationality = new JTextField();
	private JTextField jtfState = new JTextField();
	private JComboBox jcbChildren;
	private JFrame frame;

	// Main method
	public static void main(String[] args) {
		FamilyTree frame = new FamilyTree();
		frame.setTitle("Family Tree");
		frame.setSize(450, 320);
		frame.setLocationRelativeTo(null); // Center the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	// Constructor
	public FamilyTree() {

		// Add menu items to the menu
	    JMenu jMenu1 = new JMenu("File");
	    jMenu1.add(jmiSave);
	    jmiSave.addActionListener(this);
	    // Add menus to the menu bar
	    JMenuBar jMenuBar1 = new JMenuBar();
	    jMenuBar1.add(jMenu1);
	    // Set the menu bar
	    setJMenuBar(jMenuBar1);

		jtaOutput.setEditable(false);

		// Create the first tree
	    DefaultMutableTreeNode root;

	    root = new DefaultMutableTreeNode("My Family Tree");

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
			      new JScrollPane(jTree = new JTree(root)), new JScrollPane(jtaOutput));

		DefaultTreeCellRenderer renderer =
		      (DefaultTreeCellRenderer)jTree.getCellRenderer();
		    renderer.setLeafIcon(leafImageIcon);
		    renderer.setOpenIcon(branchImageIcon);
		    renderer.setClosedIcon(branchImageIcon);

		jTree.addTreeSelectionListener(this);
		JPanel panel = new JPanel();
		panel.add(jbtAdd);
	    panel.add(jbtRemove);
	    panel.add(jbtModify);

		add(splitPane, BorderLayout.CENTER);
	    add(panel, BorderLayout.NORTH);
	    // Reload the model since a new tree node is added
	      ((DefaultTreeModel)(jTree.getModel())).reload();

	    // A listener for the add button
	    jbtAdd.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	          addMember();
	        }
	    });

	    // A listener for the delete button
	    jbtRemove.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	          removeMember();
	        }
	    });

	    // A listener for the modify button
	    jbtModify.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	          modifyMember();
	        }
	    });

	    // A listener for the save button
	    jbtSave.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	          saveMemberInfo();
	        }
	    });

	    jmiSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)  {

				try {
					save();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

	    // Listener for tooltips
	    jTree.addMouseMotionListener(new MouseMotionAdapter() {
	    	public void mouseMoved(MouseEvent e) {

	    		TreePath selPath = jTree.getPathForLocation(e.getX(), e.getY());

	    		if (selPath != null) {
	    			DefaultMutableTreeNode node = (DefaultMutableTreeNode)
		            (selPath.getLastPathComponent());
	    			if (!node.isRoot()) {
	    				for (int i = 0; i < members.size(); i++) {
			        		if (members.get(i).getName().equals(node.toString())) {
			        			jTree.setToolTipText(members.get(i).toString());
			        		}
			        	}
	    			}
	    		}
	    	}
	    });

	    // Listener for pop up menu
	    jTree.addMouseListener(new MouseAdapter() {
	    	public void mouseReleased(MouseEvent e) {
	    		if (e.isPopupTrigger()) {
	    			pop.removeAll();
	    			TreePath selPath = jTree.getPathForLocation(e.getX(), e.getY());

		    		if (selPath != null) {
		    			DefaultMutableTreeNode node = (DefaultMutableTreeNode)
			            (selPath.getLastPathComponent());
		    			if (!node.isRoot()) {
		    				for (int i = 0; i < members.size(); i++) {
				        		if (members.get(i).getName().equals(node.toString())) {
				        			JTextArea jtPop = new JTextArea();
				        			jtPop.setEditable(false);
				        			jtPop.append(members.get(i).toString());
				        			pop.add(jtPop);
				        			pop.show(e.getComponent(), e.getX(), e.getY());
				        		}
				        	}
		    			}
		    		}
	    		}
	    	}
	    });
	}

	// A selection listener to print node info
	public void valueChanged(TreeSelectionEvent e) {
		printInfo();
	}

	// A listener for action performed
	public void actionPerformed (ActionEvent e) {

		if (jcbChildren.getSelectedItem() != "Edit Child") {

			jcbChildren.removeItemAt(0);
    		jcbIndex = jcbChildren.getSelectedIndex();
    		String jcbItem = jcbChildren.getSelectedItem().toString();
    		frame.dispose();
    		String kid = JOptionPane.showInputDialog(
	  	            null, "Edit name for " + jcbChildren.getSelectedItem(), "Edit Child",
	  	            JOptionPane.QUESTION_MESSAGE);
    		members.get(currNumIndex).getChildren().remove(jcbChildren.getSelectedItem().toString());
    		members.get(currNumIndex).getChildren().add(jcbIndex, kid);
    		//System.out.println("child names " + members.get(currNumIndex).getChildren());
    		TreePath path = jTree.getSelectionPath();
    		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
            (path.getLastPathComponent());

    		DefaultMutableTreeNode node1 = (DefaultMutableTreeNode) node.getChildAt(jcbIndex);
    		node1.setUserObject(kid);

    		for (int i = 0; i < members.size(); i++) {
    			if (members.get(i).getName().equals(jcbItem)) {
    				members.get(i).setName(kid);
    			}

    		}
    		modifyMember();
    		//System.out.println("child names " + members.get(currNumIndex).getChildren());
    	}

	}

	// Method to print member info
	public void printInfo() {

		jtaOutput.setText("");

		TreePath path = jTree.getSelectionPath();

    	if (path == null) {
    		return;
    	}

    	DefaultMutableTreeNode node = (DefaultMutableTreeNode)
        (path.getLastPathComponent());

    	for (int i = 0; i < members.size(); i++) {
    		if (members.get(i).getName().equals(node.toString())) {
    			jtaOutput.append(members.get(i).toString() + "\n");
    		}
    	}
	}

	// Method for adding tree nodes
	public void addMember() {
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)
        jTree.getLastSelectedPathComponent();

      if (parent == null) {
        JOptionPane.showMessageDialog(null,
          "No node in the tree is selected");
        return;
      }

      // Enter a new node
      String name = JOptionPane.showInputDialog(
        null, "Enter a child node for "+ parent, "Add a Child",
        JOptionPane.QUESTION_MESSAGE);

      int age = Integer.parseInt(JOptionPane.showInputDialog(
  	            null, "Enter age for "+ name, "Add an Age",
  	            JOptionPane.QUESTION_MESSAGE));

      String spouse = JOptionPane.showInputDialog(
	            null, "Enter name of spouse for "+ name, "Add a Spouse",
	            JOptionPane.QUESTION_MESSAGE);

      String nationality = JOptionPane.showInputDialog(
	            null, "Enter nationality for "+ name, "Add a Nationality",
	            JOptionPane.QUESTION_MESSAGE);

      String state = JOptionPane.showInputDialog(
	            null, "Enter State for "+ name, "Add a State",
	            JOptionPane.QUESTION_MESSAGE);

      members.add(new FamilyMember(name, age, spouse, nationality, state));

      while (true) {
    	  String kid = JOptionPane.showInputDialog(
	  	            null, "Enter a child for "+ name, "Add a Child",
	  	            JOptionPane.QUESTION_MESSAGE);

    	  if (!kid.equals("")) {
    		  members.get(index).setChildren(kid);
    	  }
    	  else
    		  break;
      }

      for (int i = 0; i < members.size(); i++) {
		  if (members.get(i).getName().equals(parent.toString())) {
			  members.get(i).setChildren(name);
			  parentIndex = i;
		  }
	  }

      child = new DefaultMutableTreeNode(name);

      // Insert the new node as a child of treeNode
      parent.add(child);

      if (!parent.isLeaf()) {
    	  for (int i = 0; i < parent.getChildCount() - 1; i++) {
        	  for (int j = 0; j < members.size(); j++) {
        		  if (members.get(j).getName().equals(parent.getChildAt(i).toString())) {
        			  currNumIndex = j;
        		  }
        	  }

        	  if (members.get(index).getAge() > members.get(currNumIndex).getAge()) {
        		  parent.insert(child, i);

        		  if (!parent.isRoot()) {
	        		  members.get(parentIndex).getChildren().remove(parent.getChildAt(i).toString());
	        		  members.get(parentIndex).getChildren().add(i, child.toString());
        		  }
        		  break;
        	  }
          }
      }

      if (members.get(index).getNumChildren() > 0) {

    	  for (int i = 0; i < members.get(index).getNumChildren(); i++) {
    		  members.add(new FamilyMember(members.get(index).getChildren().get(i)));
    		  child.add(new DefaultMutableTreeNode(members.get(index).getChildren().get(i)));
    	  }

    	  index = members.size();
      }
      else {
    	  jTree.setToolTipText(members.get(index).toString());
    	  index++;
      }

      // Reload the model since a new tree node is added
      ((DefaultTreeModel)(jTree.getModel())).reload();
	}

	// Method for deleting tree nodes
	public void removeMember() {
		// Get all selected paths
    	TreePath[] paths = jTree.getSelectionPaths();


    	if (paths == null) {
    		JOptionPane.showMessageDialog(null,
    		"No node in the tree is selected");
    		return;
    	}

      // Remove all selected nodes
      for (int i = 0; i < paths.length; i++) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
            (paths[i].getLastPathComponent());

        if (node.isRoot()) {
          JOptionPane.showMessageDialog(null,
            "Cannot remove the root");
        }
        else {
          for (int j = 0; j < members.size(); j++) {
        	  if (members.get(j).getName().equals(node.toString())) {
        		  for (int k = 0; k < members.size(); k++) {
        			  if (members.get(k).getChildren().contains(node.toString()))
            			  members.get(k).getChildren().remove(node.toString());
        		  }
            	  members.remove(j);
        	  }
          }
          node.removeFromParent();
        }
      }

      index = members.size();

      // Reload the model since a new tree node is added
      ((DefaultTreeModel)(jTree.getModel())).reload();
	}

	// Method to modify member information
	public void modifyMember() {

		// Get selected path
    	TreePath path = jTree.getSelectionPath();

    	if (path == null) {
    		JOptionPane.showMessageDialog(null,
    		"No node in the tree is selected");
    		return;
    	}

    	DefaultMutableTreeNode node = (DefaultMutableTreeNode)
        (path.getLastPathComponent());

    	if (node.isRoot()) {
            JOptionPane.showMessageDialog(null,
              "Cannot modify the root");
          }
          else {
        	  for (int i = 0; i < members.size(); i++) {
            	  if (members.get(i).getName().equals(node.toString()))
            		  currNumIndex = i;
        	  }

        	  JPanel jpTextField = new JPanel();
        	  jpTextField.setLayout(new GridLayout(7, 1));
        	  jpTextField.add(new JLabel("Name:"));
        	  jtfName.add(new JTextField());
        	  jtfName.setText(members.get(currNumIndex).getName());
        	  jpTextField.add(jtfName);
        	  jtfName.addActionListener(this);
        	  jpTextField.add(new JLabel("Age:"));
        	  jtfAge.add(new JTextField());
        	  jtfAge.setText(Integer.toString(members.get(currNumIndex).getAge()));
        	  jpTextField.add(jtfAge);
        	  jtfAge.addActionListener(this);
        	  jpTextField.add(new JLabel("Spouse:"));
        	  jtfSpouse.add(new JTextField());
        	  jtfSpouse.setText(members.get(currNumIndex).getSpouse());
        	  jpTextField.add(jtfSpouse);
        	  jtfSpouse.addActionListener(this);
        	  jpTextField.add(new JLabel("Children:"));
        	  jcbChildren = new JComboBox();
        	  jcbChildren.addItem("Edit Child");
        	  for (int j = 0; j < members.get(currNumIndex).getNumChildren(); j++) {
        		  jcbChildren.addItem(members.get(currNumIndex).getChildren().get(j));
        	  }
        	  jcbChildren.addActionListener(this);
        	  jpTextField.add(jcbChildren);
        	  jpTextField.add(new JLabel("Nationality:"));
        	  jtfNationality.add(new JTextField());
        	  jtfNationality.setText(members.get(currNumIndex).getNationality());
        	  jpTextField.add(jtfNationality);
        	  jtfNationality.addActionListener(this);
        	  jpTextField.add(new JLabel("State:"));
        	  jtfState.add(new JTextField());
        	  jtfState.setText(members.get(currNumIndex).getState());
        	  jpTextField.add(jtfState);
        	  jtfState.addActionListener(this);
        	  jpTextField.add(jbtSave);

        	  frame = new JFrame();
        	  frame.add(jpTextField);
        	  frame.setTitle("Modify Member");
        	  frame.setSize(400, 200);
        	  frame.setLocationRelativeTo(null);
        	  frame.setVisible(true);

          }
	}

	public void save() throws IOException {
		if (jFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
			save(jFileChooser.getSelectedFile());
	}

	private void save(File file) throws IOException {

		try {

			jtaOutput.setText("");
			jtaOutput.append(members.toString());
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
			byte[] b = (jtaOutput.getText()).getBytes();
			out.write(b, 0, b.length);
			out.close();
			jtaOutput.setText("");


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveMemberInfo() {

		String oldName = members.get(currNumIndex).getName();
		members.get(currNumIndex).setName(jtfName.getText());
		members.get(currNumIndex).setAge(Integer.parseInt(jtfAge.getText()));
		members.get(currNumIndex).setSpouse(jtfSpouse.getText());
		members.get(currNumIndex).setNationality(jtfNationality.getText());
		members.get(currNumIndex).setState(jtfState.getText());
		frame.dispose();

		TreePath path = jTree.getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
        (path.getLastPathComponent());

		node.setUserObject(jtfName.getText());

		// Reload the model since a new tree node is added
	    ((DefaultTreeModel)(jTree.getModel())).reload();
		sortChildren(node, oldName);
	}

	public void sortChildren(DefaultMutableTreeNode childNode, String oldName) {

		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)
        (childNode.getParent());
		int parIndex = 0;
		int kidIndex = 0;

		if (!parent.isLeaf()) {
			if (!parent.isRoot()) {
				for (int j = 0; j < members.size(); j++) {
		      		  if (members.get(j).getName().equals(parent.toString())) {
		      			  parIndex = j;
		      		  }
					}
					for (int j = 0; j < members.get(parIndex).getNumChildren(); j++) {
						if (members.get(parIndex).getChildren().get(j).equals(oldName)) {
							kidIndex = j;
						}
					}
					members.get(parIndex).getChildren().remove(oldName);
					members.get(parIndex).getChildren().add(kidIndex, childNode.toString());
			}
		}
	}


}

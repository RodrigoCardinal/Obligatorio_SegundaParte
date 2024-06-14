package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class App {
    private JFrame frame;
    private DefaultListModel<Resource> resourceListModel;
    private DefaultListModel<Process> processListModel;
    private Scheduler scheduler;
    private JTextField schedulingPolicyField;
    private JTextField initialTimeoutField;
    private JTextField resourceIdField;
    private JTextField resourceNameField;
    private JTextField processNameField;
    private JTextField processTimeRequiredField;
    private JTextField processInitialContextField;
    private JTextField processResourcesField;

    public App() {
        frame = new JFrame("Scheduler App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new GridLayout(4, 1));

        // Panel para agregar recursos
        JPanel resourcePanel = new JPanel();
        resourcePanel.setLayout(new GridLayout(3, 2));

        JLabel resourceIdLabel = new JLabel("Resource ID:");
        resourceIdField = new JTextField();
        JLabel resourceNameLabel = new JLabel("Resource Name:");
        resourceNameField = new JTextField();
        JButton addResourceButton = new JButton("Add Resource");

        resourceListModel = new DefaultListModel<>();
        JList<Resource> resourceList = new JList<>(resourceListModel);

        resourcePanel.add(resourceIdLabel);
        resourcePanel.add(resourceIdField);
        resourcePanel.add(resourceNameLabel);
        resourcePanel.add(resourceNameField);
        resourcePanel.add(addResourceButton);
        resourcePanel.add(new JScrollPane(resourceList));

        frame.add(resourcePanel);

        // Panel para agregar procesos
        JPanel processPanel = new JPanel();
        processPanel.setLayout(new GridLayout(5, 2));

        JLabel processNameLabel = new JLabel("Process Name:");
        processNameField = new JTextField();
        JLabel processTimeRequiredLabel = new JLabel("Time Required:");
        processTimeRequiredField = new JTextField();
        JLabel processInitialContextLabel = new JLabel("Initial Context:");
        processInitialContextField = new JTextField();
        JLabel processResourcesLabel = new JLabel("Resources Needed (comma separated IDs):");
        processResourcesField = new JTextField();
        JButton addProcessButton = new JButton("Add Process");

        processListModel = new DefaultListModel<>();
        JList<Process> processList = new JList<>(processListModel);

        processPanel.add(processNameLabel);
        processPanel.add(processNameField);
        processPanel.add(processTimeRequiredLabel);
        processPanel.add(processTimeRequiredField);
        processPanel.add(processInitialContextLabel);
        processPanel.add(processInitialContextField);
        processPanel.add(processResourcesLabel);
        processPanel.add(processResourcesField);
        processPanel.add(addProcessButton);
        processPanel.add(new JScrollPane(processList));

        frame.add(processPanel);

        // Panel para configurar el scheduler
        JPanel schedulerPanel = new JPanel();
        schedulerPanel.setLayout(new GridLayout(3, 2));

        JLabel schedulingPolicyLabel = new JLabel("Scheduling Policy:");
        schedulingPolicyField = new JTextField();
        JLabel initialTimeoutLabel = new JLabel("Initial Timeout:");
        initialTimeoutField = new JTextField();
        JButton configureSchedulerButton = new JButton("Configure Scheduler");

        schedulerPanel.add(schedulingPolicyLabel);
        schedulerPanel.add(schedulingPolicyField);
        schedulerPanel.add(initialTimeoutLabel);
        schedulerPanel.add(initialTimeoutField);
        schedulerPanel.add(configureSchedulerButton);

        frame.add(schedulerPanel);

        // Panel para iniciar el scheduler
        JPanel startPanel = new JPanel();
        JButton startButton = new JButton("Start Scheduler");
        startPanel.add(startButton);

        frame.add(startPanel);

        // Acción para agregar recurso
        addResourceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(resourceIdField.getText());
                String name = resourceNameField.getText();
                Resource resource = new Resource(id, name);
                resourceListModel.addElement(resource);
                if (scheduler != null) {
                    scheduler.addResource(resource);
                }
            }
        });

        // Acción para agregar proceso
        addProcessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = processNameField.getText();
                int timeRequired = Integer.parseInt(processTimeRequiredField.getText());
                int initialContext = Integer.parseInt(processInitialContextField.getText());
                String[] resourceIds = processResourcesField.getText().split(",");
                LinkedList<Resource> resources = new LinkedList<>();
                for (String idStr : resourceIds) {
                    int id = Integer.parseInt(idStr.trim());
                    for (int i = 0; i < resourceListModel.size(); i++) {
                        Resource resource = resourceListModel.get(i);
                        if (resource.getID() == id) {
                            resources.add(resource);
                            break;
                        }
                    }
                }
                Process process = new Process(name, timeRequired, initialContext, resources);
                processListModel.addElement(process);
                if (scheduler != null) {
                    scheduler.AddProcess(process);
                }
            }
        });

        // Acción para configurar el scheduler
        configureSchedulerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String policy = schedulingPolicyField.getText();
                int timeout = Integer.parseInt(initialTimeoutField.getText());
                scheduler = new Scheduler(policy, timeout);
                // Añadir los recursos existentes al scheduler
                for (int i = 0; i < resourceListModel.size(); i++) {
                    scheduler.addResource(resourceListModel.get(i));
                }
                // Añadir los procesos existentes al scheduler
                for (int i = 0; i < processListModel.size(); i++) {
                    scheduler.AddProcess(processListModel.get(i));
                }
                JOptionPane.showMessageDialog(frame, "Scheduler Configured!");
            }
        });

        // Acción para iniciar el scheduler
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (scheduler != null) {
                    try {
                        scheduler.Start();
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(frame, "Scheduler Started!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Configure the Scheduler first!");
                }
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new App();
            }
        });
    }
}

package com.universalmodloader.installer;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

/**
 * Interface gráfica simples do instalador.
 *
 * Em vez de um instalador “real” de Minecraft, esta janela simula a experiência de um launcher
 * de mod loader: mostra nome do projeto, pasta de instalação e botão para iniciar a instalação.
 */
public class InstallerFrame extends JFrame {

    public InstallerFrame() {
        super("UniversalModLoader Installer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(560, 260);
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel title = new JLabel("UniversalModLoader", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));

        JTextArea info = new JTextArea(
            "Este instalador cria a estrutura de diretórios do carregador híbrido.\n" +
            "Ele prepara a pasta de instalação, a pasta /mods e a pasta de configuração.\n" +
            "A partir daí, o loader pode descobrir e carregar mods Fabric e Forge/NeoForge."
        );
        info.setEditable(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setOpaque(false);

        JButton installButton = new JButton("Instalar / Preparar ambiente");
        installButton.addActionListener(e -> {
            try {
                Path installRoot = Path.of(System.getProperty("user.home"), "UniversalModLoader");
                InstallationPlan plan = InstallationPlan.create(installRoot, "UniversalModLoader");

                JOptionPane.showMessageDialog(
                    this,
                    "Instalação concluída.\nDiretório: " + plan.getInstallDir() +
                    "\nMods: " + plan.getModsDir(),
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Erro ao preparar a instalação:\n" + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(info), BorderLayout.CENTER);
        panel.add(installButton, BorderLayout.SOUTH);

        setContentPane(panel);
    }
}

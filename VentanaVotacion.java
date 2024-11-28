import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VentanaVotacion extends JFrame {
    private SistemaVotacion sistema;
    private JTextField txtId;
    private final JButton btnVotar;
    private JComboBox<String> comboCandidatos;
    private final JButton btnResultados;

    public VentanaVotacion() {
        sistema = new SistemaVotacion();
        setTitle("Sistema de Votación");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        txtId = new JTextField(15);
        btnVotar = new JButton("Votar");
        comboCandidatos = new JComboBox<>(new String[] { "Candidato 1", "Candidato 2", "Candidato 3", "Candidato 4" });
        btnResultados = new JButton("Mostrar Resultados");
        add(new JLabel("ID Votante:"));
        add(txtId);
        add(new JLabel("Seleccione un candidato:"));
        add(comboCandidatos);
        add(btnVotar);
        add(btnResultados);
        btnVotar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtId.getText();
                int candidatoIndex = comboCandidatos.getSelectedIndex();
                if (sistema.autenticarVotante(id)) {
                    sistema.votar(id, candidatoIndex);
                    JOptionPane.showMessageDialog(null, "Voto registrado con éxito.");
                } else {
                    JOptionPane.showMessageDialog(null, "Ya has votado.");
                }
            }
        });
        btnResultados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarResultados();
            }
        });
    }

    private void mostrarResultados() {
        JFrame resultadosFrame = new JFrame("Resultados");
        resultadosFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultadosFrame.setSize(400, 400);
        resultadosFrame.add(new PanelGrafico(sistema.getResultados()));
        resultadosFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaVotacion ventana = new VentanaVotacion();
            ventana.setVisible(true);
        });
    }
}

class PanelGrafico extends JPanel {
    private final ArrayList<Candidato> candidatos;

    public PanelGrafico(ArrayList<Candidato> candidatos) {
        this.candidatos = candidatos;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int barWidth = width / candidatos.size();
        int maxVotos = candidatos.stream().mapToInt(Candidato::getVotos).max().orElse(1);
        for (int i = 0; i < candidatos.size(); i++) {
            Candidato candidato = candidatos.get(i);
            int barHeight = (int) ((double) candidato.getVotos() / maxVotos * (height - 50));
            int x = i * barWidth;
            int y = height - barHeight - 30;
            g.setColor(Color.getHSBColor((float) i / candidatos.size(), 1f, 1f));
            g.fillRect(x, y, barWidth - 30, barHeight - 20);
            g.setColor(Color.BLUE);
            g.drawString(candidato.getNombre(),
                    x + (barWidth - 10) / 2 - g.getFontMetrics().stringWidth(candidato.getNombre()) / 2, height - 10);
            g.drawString(String.valueOf(candidato.getVotos()),
                    x + (barWidth - 10) / 2 - g.getFontMetrics().stringWidth(String.valueOf(candidato.getVotos())) / 2,
                    y - 10);
        }
        g.setColor(Color.BLACK);
        g.drawString("Resultados de la Votación",
                width / 2 - g.getFontMetrics().stringWidth("Resultados de la Votación") / 2, 20);
    }
}
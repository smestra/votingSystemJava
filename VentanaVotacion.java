import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VentanaVotacion extends JFrame {
    private SistemaVotacion sistema;
    private JTextField txtId;
    private JButton btnVotar;
    private JComboBox<String> comboCandidatos;
    private JButton btnResultados;

    public VentanaVotacion() {
        sistema = new SistemaVotacion();
        setTitle("Sistema de Votación");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        txtId = new JTextField(15);
        btnVotar = new JButton("Votar");
        comboCandidatos = new JComboBox<>(new String[]{"Candidato 1", "Candidato 2", "Candidato 3"});
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
    private ArrayList<Candidato> candidatos;

    public PanelGrafico(ArrayList<Candidato> candidatos) {
        this.candidatos = candidatos;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int totalVotos = candidatos.stream().mapToInt(Candidato::getVotos).sum();
        int startAngle = 0;

        for (Candidato candidato : candidatos) {
            int angle = (int) Math.round((double) candidato.getVotos() / totalVotos * 360);
            g.setColor(Color.getHSBColor((float) startAngle / 360, 1f, 1f)); // Color diferente para cada sección
            g.fillArc(50, 50, 300, 300, startAngle, angle);

            // Calcular la posición del texto
            int textAngle = startAngle + angle / 2;
            int x = (int) (200 + 100 * Math.cos(Math.toRadians(textAngle)));
            int y = (int) (200 + 100 * Math.sin(Math.toRadians(textAngle)));

            g.setColor(Color.BLACK);
            g.drawString(candidato.getNombre() + " (" + candidato.getVotos() + " votos)", x, y);

            startAngle += angle;
        }

        g.setColor(Color.BLACK);
        g.drawString("Resultados de la Votación", 150, 20);
    }
};
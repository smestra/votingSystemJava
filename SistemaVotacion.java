


import java.util.ArrayList;

public class SistemaVotacion {

    private ArrayList<Candidato> candidatos;
    private ArrayList<Votante> votantes;

    public SistemaVotacion() {
        candidatos = new ArrayList<>();
        votantes = new ArrayList<>();
        candidatos.add(new Candidato("Candidato 1"));
        candidatos.add(new Candidato("Candidato 2"));
        candidatos.add(new Candidato("Candidato 3"));
        candidatos.add(new Candidato("Candidato 4"));
    }

    public boolean autenticarVotante(String id) {
        for (Votante votante : votantes) {
            if (votante.getId().equals(id)) {
                return false;
            }
        }
        votantes.add(new Votante(id));
        return true;
    }

    public void votar(String id, int candidatoIndex) {
        if (candidatoIndex >= 0 && candidatoIndex < candidatos.size()) {
            candidatos.get(candidatoIndex).agregarVoto();
        }
    }

    public ArrayList<Candidato> getResultados() {
        return candidatos;
    }
}

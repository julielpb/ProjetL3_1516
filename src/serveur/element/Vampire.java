package serveur.element;

import java.util.HashMap;

public class Vampire extends Personnage{
	
	private static final long serialVersionUID = 1L;

	public Vampire(String nom, String groupe, HashMap<Caracteristique, Integer> caracts) {
		super(nom, groupe, caracts);
	}
}

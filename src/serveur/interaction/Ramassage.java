package serveur.interaction;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import serveur.Arene;
import serveur.element.Caracteristique;
import serveur.element.PotionTemporaire;
import serveur.vuelement.VuePersonnage;
import serveur.vuelement.VuePotion;
import utilitaires.Constantes;

/**
 * Represente le ramassage d'une potion par un personnage.
 *
 */
public class Ramassage extends Interaction<VuePotion> {

	/**
	 * Cree une interaction de ramassage.
	 * @param arene arene
	 * @param ramasseur personnage ramassant la potion
	 * @param potion potion a ramasser
	 */
	public Ramassage(Arene arene, VuePersonnage ramasseur, VuePotion potion) {
		super(arene, ramasseur, potion);
	}

	@Override
	public void interagit() {
		try {
			logs(Level.INFO, Constantes.nomRaccourciClient(attaquant) + " essaye de rammasser " + 
					Constantes.nomRaccourciClient(defenseur));
			
			// si le personnage est vivant
			if(attaquant.getElement().estVivant()) {

				// caracteristiques de la potion
				HashMap<Caracteristique, Integer> valeursPotion = defenseur.getElement().getCaracts();
				
				if(defenseur.getElement() instanceof PotionTemporaire){
					if (!(attaquant.getElement().getCaracts().isEmpty())){
						for(Map <Caracteristique, Integer> potion: attaquant.getPotionCD().keySet()){
							for(Caracteristique c: potion.keySet())
								attaquant.getElement().incrementeCaract(c, -potion.get(c));
							attaquant.getPotionCD().remove(potion);
						}
					}
					if ((Caracteristique.VIE.getMax()-attaquant.getElement().getCaract(Caracteristique.VIE))<valeursPotion.get(Caracteristique.VIE)){
						valeursPotion.put(Caracteristique.VIE, Caracteristique.VIE.getMax()-attaquant.getElement().getCaract(Caracteristique.VIE));
					}
					if ((Caracteristique.FORCE.getMax()-attaquant.getElement().getCaract(Caracteristique.FORCE))<valeursPotion.get(Caracteristique.FORCE)){
						valeursPotion.put(Caracteristique.FORCE, Caracteristique.FORCE.getMax()-attaquant.getElement().getCaract(Caracteristique.FORCE));
					}
					if ((Caracteristique.INITIATIVE.getMax()-attaquant.getElement().getCaract(Caracteristique.INITIATIVE))<valeursPotion.get(Caracteristique.INITIATIVE)){
						valeursPotion.put(Caracteristique.INITIATIVE, Caracteristique.INITIATIVE.getMax()-attaquant.getElement().getCaract(Caracteristique.INITIATIVE));
					}
					if ((Caracteristique.VITESSE.getMax()-attaquant.getElement().getCaract(Caracteristique.VITESSE))<valeursPotion.get(Caracteristique.VITESSE)){
						valeursPotion.put(Caracteristique.VITESSE, Caracteristique.VITESSE.getMax()-attaquant.getElement().getCaract(Caracteristique.VITESSE));
					}
					attaquant.ajoutePotionTemporaire(valeursPotion);
				}
				
				for(Caracteristique c : valeursPotion.keySet()) {
					arene.incrementeCaractElement(attaquant, c, valeursPotion.get(c));
					
				}
				
				logs(Level.INFO, "Potion bue !");
				
				// test si mort
				if(!attaquant.getElement().estVivant()) {
					arene.setPhrase(attaquant.getRefRMI(), "Je me suis empoisonne, je meurs ");
					logs(Level.INFO, Constantes.nomRaccourciClient(attaquant) + " vient de boire un poison... Mort >_<");
				}

				// suppression de la potion
				arene.ejectePotion(defenseur.getRefRMI());
				
			} else {
				logs(Level.INFO, Constantes.nomRaccourciClient(attaquant) + " ou " + 
						Constantes.nomRaccourciClient(defenseur) + " est deja mort... Rien ne se passe");
			}
		} catch (RemoteException e) {
			logs(Level.INFO, "\nErreur lors d'un ramassage : " + e.toString());
		}
	}
}

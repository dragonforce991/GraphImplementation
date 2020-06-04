package upo.additionalstructures;

public interface PriorityQueueDouble {
	
	/** Aggiunge l'elemento el alla coda, con priorità priority.
	 * 
	 * @param el l'elemento (o l'indice dell'elemento) da inserire.
	 * @param priority la priorità dell'elemento.
	 */
	public void enqueue(int el, double priority);
	
	/** Rimuove e restituisce l'elemento con priorità maggiore (o minore, a seconda dell'implementazione) di
	 * questa coda con priorità.
	 * 
	 * @return l'elemento con priorità maggiore (o minore, a seconda dell'implementazione) di
	 * questa coda con priorità.
	 */
	public int dequeue();
	
	/** Cambia la priorità di el.
	 * 
	 * @param el l'elemento (o l'indice dell'elemento).
	 * @param newPriority la nuova priorità di el.
	 */
	public void modify_priority(int el, double newPriority);
}

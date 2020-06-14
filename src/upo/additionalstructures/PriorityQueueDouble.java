package upo.additionalstructures;

public interface PriorityQueueDouble {
	
	/** Aggiunge l'elemento el alla coda, con priorit� priority.
	 * 
	 * @param el l'elemento (o l'indice dell'elemento) da inserire.
	 * @param priority la priorit� dell'elemento.
	 */
	public void enqueue(int el, double priority);
	
	/** Rimuove e restituisce l'elemento con priorit� maggiore (o minore, a seconda dell'implementazione) di
	 * questa coda con priorit�.
	 * 
	 * @return l'elemento con priorit� maggiore (o minore, a seconda dell'implementazione) di
	 * questa coda con priorit�.
	 */
	public int dequeue();
	
	/** Cambia la priorit� di el.
	 * 
	 * @param el l'elemento (o l'indice dell'elemento).
	 * @param newPriority la nuova priorit� di el.
	 */
	public void modify_priority(int el, double newPriority);
}

package ucp.greves.data.line.canton;

/**
 * Enumeration to determine the state of the canton
 * 
 * SLOWSDOWN : There is a slow-down on this canton (the speed of trains are reduced)
 * BLOCKED : The canton is blocked and the train can't move
 * NO_PROBLEM : No problem on the canton, the speed is normal
 * 
 * @see Canton
 */
public enum CantonState {
	NO_PROBLEM,
	SLOWSDOWN,
	BLOCKED;
}

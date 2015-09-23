package unii.draft.mtg.parings.validation;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * Reversed forEach iteration <br>
 * not used from version 0.2
 * @param <T>
 */
public class ReversedForEach<T> implements Iterable<T> {
	private final List<T> original;

	public ReversedForEach(List<T> original) {
		this.original = original;
	}

	public Iterator<T> iterator() {
		final ListIterator<T> i = original.listIterator(original.size());

		return new Iterator<T>() {
			public boolean hasNext() {
				return i.hasPrevious();
			}

			public T next() {
				return i.previous();
			}

			public void remove() {
				i.remove();
			}
		};
	}

	public static <T> ReversedForEach<T> reversed(List<T> original) {
		return new ReversedForEach<T>(original);
	}

}

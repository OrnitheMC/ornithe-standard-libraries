package net.ornithemc.osl.core.api.util.sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class TopoSort {

	public static <T> List<T> sort(Collection<T> unsortedNodes, Map<T, Set<T>> incomingEdges) {
		Map<T, Node<T>> nodes = unsortedNodes.stream()
				.collect(Collectors.toMap(Function.identity(), Node::new));

		nodes.forEach((value, node) -> {
			Set<T> nodesIn = incomingEdges.get(value);

			if (nodesIn != null && !nodesIn.isEmpty()) {
				for (T prev : nodesIn) {
					Node<T> pred = nodes.get(prev);

					if (pred != null) {
						node.addPredecessor(pred);
					}
				}
			}
		});

		return sort(nodes.values());
	}

	public static <T> List<T> sort(Collection<Node<T>> unsortedNodes) {
		List<T> sortedNodes = new ArrayList<>();

		while (!unsortedNodes.isEmpty()) {
			Iterator<Node<T>> it = unsortedNodes.iterator();

			while (it.hasNext()) {
				Node<T> node = it.next();

				if (node.precedingNodes.isEmpty()) {
					it.remove();

					sortedNodes.add(node.value);
					node.removeSuccessors();
				}
			}
		}

		return sortedNodes;
	}

	private static final class Node<T> {

		private final T value;
		private final Set<Node<T>> precedingNodes;
		private final Set<Node<T>> succedingNodes;

		public Node(T value) {
			this.value = value;
			this.precedingNodes = new HashSet<>();
			this.succedingNodes = new HashSet<>();
		}

		public void addPredecessor(Node<T> node) {
			this.precedingNodes.add(node);
			node.succedingNodes.add(this);
		}

		public void removeSuccessors() {
			Iterator<Node<T>> it = this.succedingNodes.iterator();

			while (it.hasNext()) {
				Node<T> node = it.next();

				it.remove();
				node.precedingNodes.remove(this);
			}
		}
	}
}

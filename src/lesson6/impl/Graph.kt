package lesson6.impl

import lesson6.Graph.Edge
import lesson6.Graph
import lesson6.Graph.Vertex

class GraphBuilder {

    data class VertexImpl(private val nameField: String) : Vertex {
        override fun getName() = nameField

        override fun toString() = name
    }

    data class EdgeImpl(
        private val weightField: Int,
        private val _begin: Vertex,
        private val _end: Vertex
    ) : Edge {

        override fun getBegin() = _begin

        override fun getEnd() = _end

        override fun getWeight() = weightField
    }

    private val vertices = mutableMapOf<String, Vertex>()

    private val connections = mutableMapOf<Vertex, Set<EdgeImpl>>()

    private fun addVertex(v: Vertex) {
        vertices[v.name] = v
    }

    fun addVertex(name: String): Vertex {
        return VertexImpl(name).apply {
            addVertex(this)
        }
    }

    fun addConnection(begin: Vertex, end: Vertex, weight: Int = 1) {
        val edge = EdgeImpl(weight, begin, end)
        connections[begin] = connections[begin]?.let { it + edge } ?: setOf(edge)
        connections[end] = connections[end]?.let { it + edge } ?: setOf(edge)
    }

    fun build(): Graph = object : Graph {

        override fun get(name: String): Vertex? = this@GraphBuilder.vertices[name]

        override fun getVertices(): Set<Vertex> = this@GraphBuilder.vertices.values.toSet()

        override fun getEdges(): Set<Edge> {
            return connections.values.flatten().toSet()
        }

        override fun getConnections(v: Vertex): Map<Vertex, Edge> {
            val edges = connections[v] ?: emptySet()
            val result = mutableMapOf<Vertex, Edge>()
            for (edge in edges) {
                when (v) {
                    edge.begin -> result[edge.end] = edge
                    edge.end -> result[edge.begin] = edge
                }
            }
            return result
        }

        override fun dfsVisited(vertex: Vertex, visited: HashMap<Vertex, Boolean>) {
            visited[vertex] = true
            val edges = this.getConnections(vertex)

            for (edge in edges) {
                if (edge.value.begin == vertex && !visited[edge.value.end]!!) {
                    dfsVisited(edge.value.end, visited)
                }
            }
        }
    }
}
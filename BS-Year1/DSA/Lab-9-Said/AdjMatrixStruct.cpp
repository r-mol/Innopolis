//
// Created by SaidKamalov on 24.03.2022.
//
#include <iostream>
#include <list>
#include <vector>

using namespace std;

template<class V>
class Vertex{
    V data;
    Vertex<V>** list_position;  // pointer to pointer in the list that points to the current node
    int index;  // index in the Adjacency Matrix
public:
    Vertex(V data){
        this->data = data;
        /*this->list_position = list_position;
        this->index = index;*/
    }
    Vertex(){};
    V getData(){
        return this->data;
    }
    Vertex<V>** getList_position(){
        return this->list_position;
    }
    int getIndex(){
        return this->index;
    }
    void setData(V data){
        this->data = data;
    }
    void setList_position(Vertex<V>** pos){
        this->list_position = pos;
    }
    void setIndex(int index){
        this->index = index;
    }
};

template<class V, class E>
class Edge{
    E data;
    Vertex<V>* start;
    Vertex<V>* end;
    Edge<V,E>** list_position;
public:
    Edge(E data, Vertex<V>* start, Vertex<V>* end){
        this->data = data;
        this->start = start;
        this->end = end;
        //this->list_position = list_position;
    }
    Edge(){};
    E getData(){
        return this->data;
    }
    Vertex<V>* getStart(){
        return this->start;
    }
    Vertex<V>* getEnd(){
        return this->end;
    }
    Edge<V,E>** getList_position(){
        return this->list_position;
    }
    void setData(E data){
        this->data = data;
    }
    void setStart(Vertex<V>* start){
        this->start = start;
    }
    void setEnd(Vertex<V>* end){
        this->end = end;
    }
    void setList_position(Edge<V,E>** pos){
        this->list_position = pos;
    }
};

template<class V,class E>
class GraphADT {
public:
    virtual Vertex<V>* insertVertex(V data) = 0;

    virtual Edge<V,E>* insertEdge(Vertex<V>* start, Vertex<V>* end, E data)=0;

    virtual void removeVertex(Vertex<V>* vertex)=0;

    virtual void removeEdge(Edge<V,E>* edge)=0;

    virtual vector<Edge<V,E>> incidentEdges(Vertex<V>* const & vertex)=0;

    virtual vector<Vertex<V>> endVertices(Edge<V,E>* const &edge)=0;

    virtual Vertex<V> opposite(Vertex<V>* const & vertex, Edge<V,E>* const & edge)=0;

    virtual bool areAdjacent(Vertex<V>* const & vertex1,Vertex<V>* const & vertex2)=0;
};

template<class V,class E>
class GraphImplementation : GraphADT<V,E>{
    list<Vertex<V>*> vertex_list;
    //list<Edge<V,E>*> edge_list;
    vector<vector<Edge<V,E>*>> Adjacency_Matrix;
public:
    list<Edge<V,E>*> edge_list;
    virtual Vertex<V>* insertVertex(V data){
        Vertex<V>* vrtx = new Vertex(data);

        // add new vertex to the matrix -> create new row + extend size of each row with one(creating new column)
        if(Adjacency_Matrix.empty()){
            Adjacency_Matrix.push_back({});
            Adjacency_Matrix.at(0).resize(1);
        }else{
            Adjacency_Matrix.push_back({});
            for(int i = 0;i<Adjacency_Matrix.size();++i){
                Adjacency_Matrix.at(i).resize(Adjacency_Matrix.size());
            }
        }

        vrtx->setIndex(Adjacency_Matrix.size()-1);  // set reference to the index in matrix
        this->vertex_list.push_back(vrtx);  //  add vertex to the vertex list
        vrtx->setList_position(&vertex_list.back());    //  set reference to the node in the vertex list
        return vrtx;
    }

    virtual Edge<V,E>* insertEdge(Vertex<V>* start, Vertex<V>* end, E data){
        Edge<V,E>* edge = new Edge(data,start,end);
        edge_list.push_back(edge);  // add edge to the edge list
        edge->setList_position(&edge_list.back());  //  set reference to the node in the edge list

        //  put ptr to the edge in the proper cell of Adj Matrix
        Adjacency_Matrix.at(start->getIndex()).at(end->getIndex()) = edge;
        Adjacency_Matrix.at(end->getIndex()).at(start->getIndex()) = edge;
        return edge;
    }

    vector<Edge<V,E>> incidentEdges(Vertex<V>* const & vertex){
        vector<Edge<V,E>> incident_list = {};
        int index = vertex->getIndex(); //  get index of the vertex in the Matrix

        //  adding incident edges
        for(int i = 0;i<Adjacency_Matrix.at(index).size();++i){
            if(Adjacency_Matrix.at(index).at(i)!= nullptr){
                incident_list.push_back(*Adjacency_Matrix.at(index).at(i));
            }
        }
        return incident_list;
    }

    virtual vector<Vertex<V>> endVertices(Edge<V,E>* const & edge){
        vector<Vertex<V>> vertex_pair = {};
        vertex_pair.push_back(*edge->getStart());
        vertex_pair.push_back(*edge->getEnd());
        return vertex_pair;
    }

    virtual Vertex<V> opposite(Vertex<V>* const & vertex, Edge<V,E>* const & edge){
        Vertex<V> vrtx;
        if(edge->getStart() == vertex){
            vrtx = *edge->getEnd();
        }else{
            vrtx = *edge->getStart();
        }
        return vrtx;
    }

    virtual bool areAdjacent(Vertex<V>* const & vertex1,Vertex<V>* const & vertex2){
        int index_column = vertex1->getIndex();
        int index_row = vertex2->getIndex();
        if(Adjacency_Matrix.at(index_row).at(index_column) == nullptr ){
            return false;
        }
        return true;
    }

    virtual void removeEdge(Edge<V,E>* edge){
        int row_index = edge->getStart()->getIndex();
        int column_index = edge->getEnd()->getIndex();
        Adjacency_Matrix.at(row_index).at(column_index) = nullptr;
        Adjacency_Matrix.at(column_index).at(row_index) = nullptr;
        delete *edge->getList_position();
        //delete edge;
    }

    virtual void removeVertex(Vertex<V>* vertex){
        vector<Edge<V,E>> incidents = this->incidentEdges(vertex);
        for (int i = 0;i<incidents.size();++i){
            this->removeEdge(&incidents.at(i));
        }
        for(int i = 0;i<Adjacency_Matrix.size();++i){
            vector<Edge<V,E>*>& vec = Adjacency_Matrix.at(i);
            vec.erase(vec.cbegin()+vertex->getIndex());
        }
        Adjacency_Matrix.erase(Adjacency_Matrix.cbegin()+vertex->getIndex());
        delete *vertex->getList_position();
    }
};

int main(){
    GraphImplementation<int,string> graph;
    list<Edge<int,string>*>& edge_list = graph.edge_list;
    Vertex<int>* v1 = graph.insertVertex(5);
    Vertex<int>* v2 = graph.insertVertex(3);
    Edge<int,string>* edge1 = graph.insertEdge(v1,v2,"a");
    Vertex<int>* v3 = graph.insertVertex(7);
    Edge<int,string>* edge2 = graph.insertEdge(v2,v3,"b");
    vector<Edge<int,string>> vec = graph.incidentEdges(v2);
    vector<Vertex<int>> start_end_edge1 = graph.endVertices(edge1);
    //start_end_edge1.erase(start_end_edge1.cbegin();
    Vertex<int> opposite_to_3_edge1 = graph.opposite(v2,edge1);
    Vertex<int> opposite_to_3_edge2 = graph.opposite(v2,edge2);
    /*cout<<graph.areAdjacent(v1,v2)<<endl;
    cout<<graph.areAdjacent(v1,v3)<<endl;
    cout<<graph.areAdjacent(v2,v3)<<endl;
    graph.removeEdge(edge2);
    cout<<graph.areAdjacent(v2,v3)<<endl;*/
    graph.removeVertex(v2);

    return 0;
}
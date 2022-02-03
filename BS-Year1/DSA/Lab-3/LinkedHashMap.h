#ifndef LINKEDHASHMAP_H_
#define LINKEDHASHMAP_H_

#include <iostream>
#include <array>
#include <list>
#include "Ilinkedhashmap.h"
#include <string>
#include <sstream>
#include <optional>

using namespace std;

template<class K, class V>
class Entry {

	K key;
	V value;

public:

	Entry(K k, V v) :
			key(k), value(v) {
	}

	void setValue(V v) {
		this->value = v;
	}

	void setKey(K k) {
		this->key = k;
	}

	V getValue() {
		return this->value;
	}

	K getKey() {
		return this->key;
	}

	bool operator==(Entry a) {
		return a.key == this->key && a.value == this->value;
	}

};

template<class K, class V, int CAPACITY>
class LinkedHashMap: public ILinkedHashMap<K, V> {

	array<list<Entry<K, V>>, CAPACITY> buckets;

public:

// We can pass the capacity of the hash table to the constructor
//	LinkedHashMap(int capacity){
//		this->CAPACITY = capacity;
//	}

	virtual int getIndex(K key) {

		// taking an instance of hash struct
		auto hashfunc = hash<K>();

		// hashing function
		int hashcode = hashfunc(key);
		// the hashcode is of type unsigned int

		// the hashcode could be negative so we use abs function
		// to take the absolute value of the hashcode

		// compression function
		return abs(hashcode) % CAPACITY;
	}

	virtual int size() {

		int temp = 0;
		for (auto bucket : buckets) {
			if (bucket.size() > 0)
				temp+=bucket.size();
//			for (auto entry : bucket) {
//				temp++;
//			}
		}

		return temp;
	}

	virtual bool isEmpty() {
		return size() == 0;
	}

	virtual int capacity(){
		return CAPACITY;
	}

	virtual double loadFactor(){
		return 1.0 * size()/capacity();
	}

	virtual Entry<K, V>* findFirst(K key) {
		int index = getIndex(key);

		// Pointer to the bucket of the key
		auto bucketP = &buckets[index];

		if (!bucketP->empty()) {

			for (auto it = bucketP->begin(); it != bucketP->end(); it++) {

//				if ((*it).getKey() == key) {

				if (it->getKey() == key) {
					return &(*it);
				}
			}

		}

		return NULL;
	}

	virtual bool containsKey(K key) {
		auto entry_p = findFirst(key);
		return entry_p != NULL;
	}

	virtual void put(K key, V value) {

		int index = getIndex(key);

		// Pointer to the bucket of the key
		auto bucketP = &buckets[index];

		// the key is not in the hashmap
		if (!containsKey(key)) {

			// insert a new entry
			Entry<K, V> entry(key, value);
			bucketP->push_back(entry);
		} else {
			// the key is in the hashmap
			//update the value of the key

			Entry<K, V> *entry_p = findFirst(key);
			entry_p->setValue(value);
		}
	}

	virtual void remove(K key) {
		if (containsKey(key)) {
			int index = getIndex(key);

			auto bucketP = &buckets[index];

			// pointer to the entry of the key
			auto entryP = findFirst(key);

			// We are passing the entry of the key
			bucketP->remove(*entryP);
		}
	}

	virtual optional<V> get(K key) {

		if (containsKey(key)) {
			auto entryP = findFirst(key);

			if (entryP != NULL) {
				return entryP->getValue();
			}
		}

		return {};
	}

	string toString() {

		ostringstream s;
		s << "size = " << size() << " , "<<" capacity = "<< capacity() << " , load factor = " << loadFactor() << endl << "buckets = {" << endl;
		for (auto bucket : buckets) {
			s << "entries [";
			for (auto entry : bucket) {
				s << "( " << entry.getKey() << " , " << entry.getValue()
						<< " ), ";
			}
			s << "]" << endl;
		}
		s << "}" << endl;

		return s.str();
	}

};

#endif /* LINKEDHASHMAP_H_ */

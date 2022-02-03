/*
 * linkedhashmap.h
 *
 *  Created on: Jan 27, 2022
 *      Author: firasj
 */

#ifndef ILINKEDHASHMAP_H_
#define ILINKEDHASHMAP_H_

#include <optional>
#include <iostream>

using namespace std;

template <class K, class V>
class ILinkedHashMap{

public:
	// destructor is not mandatory
	virtual ~ILinkedHashMap(){};
	virtual int size() = 0;
	virtual bool isEmpty() = 0;
	virtual void put(K key, V value) = 0;
	virtual void remove(K key) = 0;
	virtual optional<V> get(K key) = 0;
};

#endif /* ILINKEDHASHMAP_H_ */

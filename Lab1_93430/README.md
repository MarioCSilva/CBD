# Lab 1 Bases de Dados Chave-Valor
## Fonte destes apontamentos: http://intro2libsys.info/introduction-to-redis/redis-data-types

# 1.1 Redis
### Start the Redis Server 
    $ redis-server 

### Start a Redis Command Line Client
    $ redis-cli

# Redis Data Types & Commands
A real strength of Redis is its powerful data structures that can be used to model and solve complex problems even with such primitive structures as Strings, Lists, Hashes, Sets, and Sorted Sets. Other data structures are stored as strings but have Redis commands for special uses. These are the Bitmap data type and the HyperLogLogs data type.

## Keys
Redis keys are binary safe -any binary stream can be used as a key- although the most common (and recommended) stream to use is a string key, like "Person", other file formats and binary streams like images, mp3, or other file formats, can be used.
### Critical Commands for Keys
    EXISTS key
Returns **1** if the specified key exists. Returns **0** otherwise.

    TYPE key
Returns the string representation of the type of the value stored at `key` (can be returned: `string`, `list`, `set`, `zset`, `hash` and `stream`).

    DEL key
Removes the specified keys. A key is ignored if it does not exist.
Returns the number of keys that were removed.

    KEYS pattern
Returns all keys matching `pattern`.

    SCAN pattern cursor
Returns an array of two values: the first value is the new cursor to use in the next call, the second value is an array of elements.

## Strings
In Redis, a string is not merely alphanumeric characters as strings are normally understood to be in higher-level programming languages, but are serialized characters in C. Integers are stored in Redis as a string.
### Critical Commands for String
    SET key string optional nx|xx
Set `key` to hold the string `value`.

    GET key
Get the value of `key`. If the key does not exist the special value `nil` is returned. 

    INCR key
Increments the number stored at `key` by one.

    INCRBY key integer
Increments the number stored at `key` by `increment`.

    DECR key
Decrements the number stored at `key` by one.

    DECRBY key integer
Decrements the number stored at `key` by `decrement`.

    APPEND key string
If `key` already exists and is a string, this command appends the `value` at the end of the string.

    MSET key1 string key2 string
Sets the given keys to their respective values. MSET replaces existing values with new values, just as regular SET.

    MGET key1 key2 key3
Returns the values of all specified keys.

## List
Lists in Redis are ordered collections of Redis strings that allows for duplicates values. Because Redis lists are implemented as linked-lists, adding an item to the front of the list with LPUSH or to the end of the list with RPUSH are relatively inexpensive operations that are performed in constant time of O(1).
### Critical Commands for List
    LPUSH key value
Insert all the specified values at the head of the list stored at `key`.
    
    RPUSH key value
Insert all the specified values at the tail of the list stored at `key`.
    
    LRANGE key start end
Getting the values of a List from index start to index end.

    LPOP key
Remove the first element from the list.

    RPOP key
Remove the last element of the list.

    LINDEX key index
Returns the element at index `index` in the list stored at `key`.

    LINSERT key BEFORE|AFTER pivot value
Inserts `value` in the list stored at `key` either before or after the reference value `pivot`.

## Hash
Hash data structure maps one or more fields to corresponding value pairs. In Redis, all hash values must be Redis strings with unique field names.
### Critical Commands for Hash    
    HSET key field value
Sets `field` in the hash stored at `key` to `value`.
    
    HGET key field
Returns the value associated with `field` in the hash stored at `key`.
    
    HMSET key field1 value1 [field2 value2 …]
Sets the specified fields to their respective values in the hash stored at `key`.
    
    HMGET key field [field2 …]
Returns the values associated with the specified `fields` in the hash stored at `key`.    
    
    HGETALL key
Returns all the values of the hash stored at `key`.    
    
    HEXISTS key field
Returns if `field` is an existing field in the hash stored at `key`.    
    
    HLEN key
Returns the number of fields contained in the hash stored at `key`.    
    
    HKEYS key
Returns all field names in the hash stored at `key`.    
    
    HVALS key
Returns all values in the hash stored at `key`.    
    
    HDEL key field
Removes the specified fields from the hash stored at `key`.    
    
    HINCRBY key field increment
Increments the number stored at `field` in the hash stored at `key` by `increment`.

## Set
A set is a collection of string values where uniqueness of each member is guaranteed but does not offer ordering of members. Redis sets also implement union, intersection, and difference set semantics along with the ability to store the results of those set operations as a new Redis.
### Critical Commands for Set
    SADD key member [member …]
Add the specified members to the set stored at `key`.

    SMEMBERS key
Returns all the members of the set value stored at `key`.

    SISMEMBER key member
Returns if `member` is a member of the set stored at `key`.

    SCARD key
Returns the set cardinality (number of elements) of the set stored at `key`.

    SUNION key1 key2 [key3 …]
Returns the members of the set resulting from the union of all the given sets.

    SINTER key1 key2 [key3 …]
Returns the members of the set resulting from the intersection of all the given sets.

    SDIFF key1 key2 [key3 …]
Returns the members of the set resulting from the difference between the first set and all the successive sets.

## Sorted Set
A sorted set combines characteristics of both Redis lists and sets. Like a Redis list, a sorted set's values are ordered and like a set, each value is assured to be unique. The flexibility of a sorted set allows for multiple types of access patterns depending on the needs of the application.
### Critical Commands for Sorted Set
    ZADD key score member [score member …]
Adds all the specified members with the specified scores to the sorted set stored at `key`.

    ZRANGE key start stop [WITHSCORES]
Returns the specified range of elements in the sorted set stored at `key`. The elements are considered to be ordered from the lowest to the highest score. Lexicographical order is used for elements with equal score.

    ZREVRANGE key start stop [WITHSCORES]
Returns the specified range of elements in the sorted set stored at `key`. The elements are considered to be ordered from the highest to the lowest score. Descending lexicographical order is used for elements with equal score.

    ZRANK key start stop [WITHSCORES]
Returns the rank of `member` in the sorted set stored at `key`, with the scores ordered from low to high. The rank (or index) is 0-based, which means that the member with the lowest score has rank `0`.

    ZSCORE key member
Returns the score of `member` in the sorted set at `key`.

    
    ZREM key member
Removes the specified members from the sorted set stored at `key`. Non existing members are ignored.

    ZCARD key
Returns the sorted set cardinality (number of elements) of the sorted set stored at `key`.

    ZCOUNT key min max
Returns the number of elements in the sorted set at `key` with a score between `min` and `max`.

    ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]
Returns all the elements in the sorted set at `key` with a score between `min` and `max` (including elements with score equal to `min` or `max`). The elements are considered to be ordered from low to high scores.



# Redis – Inserção massiva
Foi feito um script para ler o ficheiro "females.txt" e escrever noutro ficheiro "initials4redis.txt" com os comandos necessários inserir no servidor redis da seguinte forma:

    SET a 463
    SET b 257
    SET c 463
    ...

Depois foi utilizado um comando do Redis que permite fazer inserção de dados em massa a partir de um ficheiro:
    cat initials4redis.txt | redis-cli --pipe
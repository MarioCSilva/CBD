

# 2.1 MongoDB – Instalação e exploração por linha de comandos

Source: https://www.tutorialspoint.com/mongodb/mongodb_advantages.htm

## Storage Structures
-	A server contains multiple Databases, which in turn have several Collections that possess some Documents.

-	Making an analogy with non-document oriented databases, **Collections** correspond to **Tables**, whilst **Documents** correspond to **Tuples/Rows**, in which each of the Document's **Fields** are the **Columns**

### Database

​	Database is a physical container for collections. Each database gets its own set of files on the file system. A single MongoDB server typically has multiple databases.

### Collections

​	Collection is a group of MongoDB documents. It is the equivalent of an RDBMS table. A collection exists within a single database. Collections do not enforce a schema. Documents within a collection can have different fields. Typically, all documents in a collection are of similar or related purpose.

### Documents

​	A document is a set of key-value pairs. Documents have dynamic schema. Dynamic schema means that documents in the same collection do not need to have the same set of fields or structure, and common fields in a collection's documents may hold different types of data.

Any relational database has a typical schema design that shows number of tables and the relationship between these tables. While in MongoDB, there is no concept of relationship.

## Advantages of MongoDB over RDBMS

- **Schema less** − MongoDB is a document database in which one collection holds different documents. Number of fields, content and size of the document can differ from one document to another.
- Structure of a single object is clear.
- No complex joins.
- Deep query-ability. MongoDB supports dynamic queries on documents using a document-based query language that's nearly as powerful as SQL.
- Tuning.
- **Ease of scale-out** − MongoDB is easy to scale.
- Conversion/mapping of application objects to database objects not needed.
- Uses internal memory for storing the (windowed) working set, enabling faster access of data.

## Why Use MongoDB?

- **Document Oriented Storage** − Data is stored in the form of JSON style documents.
- Index on any attribute
- Replication and high availability
- Auto-Sharding
- Rich queries
- Fast in-place updates
- Professional support by MongoDB

## Where to Use MongoDB?

- Big Data
- Content Management and Delivery
- Mobile and Social Infrastructure
- User Data Management
- Data Hub

## MongoDB Commands

Todos os comandos usados e explicados no ficheiro 1/CBD_L201_93430.txt.

# 2.2  MongoDB – Construção de queries

Criar uma base de dados na instalação de mongo local com os dados presentes no ficheiro restaurants.json:

​	`$ mongoimport --db cbd --collection rest --drop --file <path/>restaurants.json`

Verificar se funcionou corretamente:

```
$ ./mongo
> use cbd
> db.rest.count() 
3772
```

Respostas no ficheiro 2/CBD_L202_93430.txt.

# 2.3  MongoDB – Funções do lado do servidor

## c)
`db.phones.aggregate([{$group: {_id:"$components.prefix", count: {$sum: 1}}}])`

## d)
```
function capicua() {
    var list = db.phones.find({},{"display":1, "_id":0}).toArray();

    var capicua = [];
    for (var i = 0; i<list.length; i++){
        var phone = list[i].display.split('-')[1];

        var rev=phone.split("").reverse().join("");
        if (phone === rev){
            capicua.push(phone);
        }
    }
    return capicua;
}
```

# 2.4  MongoDB – Driver

Escolhi um dataset com mais de 250 países que segue a seguinte estrutura:

```
{
"_id" : ObjectId("55a0f42f20a4d760b5fc3069"),
"altSpellings" : [
  "AZ",
  "Republic of Azerbaijan",
  "Azərbaycan Respublikası"
],
"area" : 86600,
"borders" : [
  "ARM",
  "GEO",
  "IRN",
  "RUS",
  "TUR"
],
"callingCode" : [
	"994"
],
"capital" : "Baku",
"cca2" : "AZ",
"cca3" : "AZE",
"ccn3" : "031",
"cioc" : "AZE",
"currency" : [
	"AZN"
],
"demonym" : "Azerbaijani",
"landlocked" : true,
"languages" : {
	"aze" : "Azerbaijani",
	"rus" : "Russian"
},
"latlng" : [
  40.5,
  47.5
],
"name" : {
	"common" : "Azerbaijan",
	"native" : {
		"aze" : {
			"common" : "Azərbaycan",
			"official" : "Azərbaycan Respublikası"
		},
		"rus" : {
			"common" : "Азербайджан",
			"official" : "Азербайджанская Республика"
			}
		},
		"official" : "Republic of Azerbaijan"
	},
	"region" : "Asia",
	"subregion" : "Western Asia",
	"tld" : [
		".az"
	],
	"translations" : {
		"cym" : {
			"common" : "Aserbaijan",
			"official" : "Republic of Azerbaijan"
			},
		"deu" : {
			"common" : "Aserbaidschan",
			"official" : "Republik Aserbaidschan"
		},
		"fin" : {
			"common" : "Azerbaidzan",
			"official" : "Azerbaidzanin tasavalta"
		},
		"fra" : {
			"common" : "Azerbaïdjan",
			"official" : "République d'Azerbaïdjan"
		},
		"hrv" : {
			"common" : "Azerbajdžan",
			"official" : "Republika Azerbajdžan"
		},
		"ita" : {
			"common" : "Azerbaijan",
			"official" : "Repubblica dell'Azerbaigian"
		},
		"jpn" : {
			"common" : "アゼルバイジャン",
			"official" : "アゼルバイジャン共和国"
		},
		"nld" : {
			"common" : "Azerbeidzjan",
			"official" : "Republiek Azerbeidzjan"
		},
		"por" : {
			"common" : "Azerbeijão",
			"official" : "República do Azerbaijão"
		},
		"rus" : {
			"common" : "Азербайджан",
			"official" : "Азербайджанская Республика"
		},
		"spa" : {
			"common" : "Azerbaiyán",
		"official" : "República de Azerbaiyán"
		}
	}
}
```



#### Queries com o operador **find()**:

##### 1. Encontrar os países que fazem fronteira com Rússia (RUS):

```
db.countries.find(
{borders:{$in:["RUS"]}},
{"name.common": 1, _id:0, borders:1} ).pretty()
```

##### 2. Listar os países com latitude superior entre 35 a 40 e longitude entre 15 e 42:

```
db.countries.find(
{ "latlng.0": {$gt:35, $lt:40},
"latlng.1": {$lt:42, $gt:15} },
{"name.common":1, _id:0, latlng:1 }).pretty()
```

##### 3. Listar todos os países que falem português ('por'):

```
db.countries.find(
{"languages.por":{"$exists":true}},
{"name.common": 1, _id:0, languages:1}).pretty()
```

##### 4. Listar 3 países que usem a moeda euro (EUR) ordenados pelo nome:

```
db.countries.find( 
{ "currency":{$in:["EUR"]} },
{ _id:0, currency:1, "name.common": 1}
).limit(3).sort({"name.common":1}).pretty()
```

##### 5. Encontrar o país com maior área da América:

```
`db.countries.find(
{region:"Africa"},
{ _id:0, region:1, "name.common": 1, area:1}
).limit(1).sort({area:-1}).pretty()
```

##### 6. Encontrar o país com o diminutivo igual a BR:

```
db.countries.find( 
{ "altSpellings":{$in:["BR"]} },
{ _id:0, "name.common": 1, altSpellings:1} ).pretty()
```



#### Queries com o operador **aggregate()**:

##### 1. Contar o número de países em cada região:

```
db.countries.aggregate(
{"$group" : {
  _id:"$region",
  count:{
  $sum:1
  }
 }}) 
```

##### 2. Encontrar o país com mais países fronteiros da Europa:

```mongo
db.countries.aggregate([
	 { "$match": { "region": "Europe" } },
   { "$group": {
       "_id": "$_id",
       "count": {
           "$sum": { "$size": "$borders" }
       }
   }}, {$sort:{count:-1}}, {$limit:1}
])
```

##### 3. Listar todos os nomes alternativos de Espanha e Portugal:

```mongo
db.countries.aggregate( 
  { $unwind : "$altSpellings" },
  { $match : { "name.common":{$in:["Spain","Portugal"]}} },
  { $project: {       "name.common": 1,       "altSpellings": 1,  _id:0   } } ).pretty()
```

##### 4. Encontrar a sub-região com mais países:

```
db.countries.aggregate(
{"$group":
	{_id:"$subregion",
	count:{$sum:1}}},
{$sort: {count:-1}},
{$limit:1})
```

##### 5. Listar as regiões e o valor da área do país mais pequeno da região:

```
db.countries.aggregate([{$group:{_id:"$region", minlat:{$min:"$area"}}  }])
```

##### 6. Contar o número de países com latitude entre 30 e 45 e longitude entre 10 e 40 por região:

```
db.countries.aggregate(
{$match: 
	{ "latlng.0": {$gt:30, $lt:45},
	"latlng.1": {$lt:40, $gt:10} },
},
{"$group" : {
  _id:"$region",
  count:{
  $sum:1
  }
 }}) 
```

##### 7. Contar o número de moedas utilizadas em países do Norte de América (não distintas):

```
db.countries.aggregate([ 
{ $match: 
	{"subregion":"Northern America"}
}, { $group: 
	{ _id:"$subregion",
	num_cur:
		{ $sum: { "$size": "$currency" } }
} } ])
```

##### 8. Contar a área total para cada uma das regiões:

```
db.countries.aggregate([
{ $group:
  { _id:"$region",
  total_area: {
    $sum: "$area"
  }}
}]).pretty()
```


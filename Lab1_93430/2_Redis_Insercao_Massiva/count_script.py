import string
list=list(string.ascii_lowercase)
map={}
for x in list:
    map[x]=0
f=open("female-names.txt")
for w in f:
    map[w[0]]+=1

f.close()

f=open("initials4redis.txt", "w")

for k in map:
    f.write("SET "+str(k)+" "+ str(map[k])+"\n")
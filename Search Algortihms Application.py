import os

dict={}
cost={}
global input
input=[]
def read_input():
	print "input reading"
	with open("input.txt",'r') as file :
		algo=file.readline()
		start=file.readline()
		goal=file.readline()
		inp=file.readlines()
	file.close()
	
		
	for x in inp:
		cello=x.split('\t')
		inter=[y.strip()for y in cello]
		input.append(inter)
		if dict.get(cello[0]):
			dict[cello[0]]=dict.get(cello[0])+[cello[1]]
		else:
			dict[cello[0]]=[cello[1]]
		
	return algo,start,goal		
			
def bfs(start,goal):
	
	bfs_queue=[]
	path_queue=[]
	print '-------------'
	bfs_queue.append(start)
	path_queue.append([start])
	cost[start]=0
	while bfs_queue:
		
		st=bfs_queue.pop(0)
		p=path_queue.pop(0)	
			
				
		if st==goal:
			print "Goal Reached"
			break
				
		if st in dict:
			for adj in dict[st]:
					bfs_queue.append(adj)
					path=list(p)
					path.append(adj)
					path_queue.append(path)
					cost[adj]=cost[st]+1
					
		
		
		
	print p
	
		
	with open('output.txt','w') as f:
		for x in p:
			f.write(x)
			f.write("\t")
			f.write(str(cost[x]))
			f.write("\n")
			
	f.close()
	
	with open('output.txt','r')as fi:
		fil=fi.readlines()
		for xv in fil:
			print xv
	fi.close()
			
		

def ucs():
#Compute cost function:
	i=0
	total_cost=0
	while i+1<len(p):
		am=p[i]
		ma=p[i+1]
		print am," ",ma
		for ino in input:
			if am==ino[0] and ma==ino[1]:
				total_cost=total_cost+int(ino[2])
				break
		i=i+1		
		print ma,"\t",total_cost
	
		
algo,start,goal=read_input()
print algo,start,goal
if algo.strip()=='BFS':
	bfs(start.strip(),goal.strip())

import sys
import re
import time
from itertools	 import chain, combinations
from collections import defaultdict

def subsets(arr):
    """ Returns non empty subsets of arr"""
    return chain(*[combinations(arr,i + 1) for i,a in enumerate(arr)])

def returnItemsWithMinSupport(itemSet, transactionList, minSupport, freqSet):
	"""calculates the support for items in the itemSet and returns a subset of the itemSet 
	each of whose elements satisfies the minimum support"""
	_itemSet = set()
	localSet = defaultdict(int)
	for item in itemSet:
		for transaction in transactionList:
			if item.issubset(transaction):
				freqSet[item] 	+= 1
				localSet[item]	+= 1
	for item,count in localSet.items():
		support = float(count)/len(transactionList)
		
		if support >= minSupport:
			_itemSet.add(item)
	return _itemSet

def joinSet(itemSet,length):
	"""Join a set with itself and returns the n-element itemsets"""
	return set([i.union(j) for i in itemSet for j in itemSet if len(i.union(j)) == length])

def getItemSetTransactionList(data_iterator):
    transactionList	= list()
    itemSet		= set()
    for record in data_iterator:
        transaction = frozenset(record)
        transactionList.append(transaction)
        for item in transaction:
            itemSet.add(frozenset([item]))		# Generate 1-itemSets
    return itemSet, transactionList

def runApriori(data_iter, minSupport):
    """
    run the apriori algorithm. data_iter is a record iterator
    Return both: 
     - items (tuple, support)
    """
    itemSet, transactionList = getItemSetTransactionList(data_iter)
    freqSet		= defaultdict(int)
    largeSet		= dict()     # Global dictionary which stores (key=n-itemSets,value=support) which satisfy minSupport
    oneCSet		= returnItemsWithMinSupport(itemSet, transactionList, minSupport, freqSet)
    currentLSet	= oneCSet
    k = 2
    while(currentLSet != set([])):
        largeSet[k-1] 	= currentLSet
        currentLSet 	= joinSet(currentLSet,k)
        currentCSet 	= returnItemsWithMinSupport(currentLSet, transactionList, minSupport, freqSet)
        currentLSet 	= currentCSet
        k = k + 1

    def getSupport(item):
            """local function which Returns the support of an item"""
            return float(freqSet[item])/len(transactionList)

    toRetItems=[]
    for key,value in largeSet.items():
        toRetItems.extend([(tuple(item), getSupport(item)) 
                           for item in value])
    return toRetItems

def printResults(items):
    """prints the generated itemsets"""
    for item, support in items:
        print "item: %s , %.5f" % (str(item), support)

def dataFromFile(fname):
	"""Function which reads from the file and yields a generator"""
	file_iter = open(fname, 'rU')
   	for line in file_iter:
		line = line.strip().rstrip(',')				# Remove trailing comma
		record = frozenset(line.split(','))
		yield record

if __name__ == "__main__":
    start = time.clock()
    if len(sys.argv) < 3:
        print 	"""Insufficient Arguments\n
		 Usage :\n
		 \tpython apriori.py <DATASET.csv> <minSupport>"""
        sys.exit('System will exit')
    
    minSupport		= float(sys.argv[2])
    items		= runApriori(dataFromFile(sys.argv[1]), minSupport)
    printResults(sorted(items, key=lambda item: item[1], reverse=True))
    print 'Code time %.5f seconds' % (time.clock() - start)

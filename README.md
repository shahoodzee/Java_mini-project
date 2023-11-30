# Java_mini-project

## Question #1
Relation scheme `ebay_prod_vendor` into a set of relational schemes while ensuring a lossless-join decomposition. 
The goal is to create smaller relations that preserve the information in the original relation.

Given Functional Dependencies (FDs):
1. \( \text{prod\_id} \rightarrow \text{price, model\_num} \)
2. \( \text{vendor, storage} \rightarrow \text{delivery\_price} \)

To achieve lossless-join decomposition, we need to find a set of smaller relations whose natural joins will cover the original relation `ebay_prod_vendor`. 

Let's perform the decomposition:

#### Decomposition:

1. **Create a Relation for FD1:**
   - Relation Name: `R1(prod_id, price, model_num)`
   - Reason: FD1 indicates that the attributes \( \text{prod\_id, price, model\_num} \) are functionally dependent on each other.

2. **Create a Relation for FD2:**
   - Relation Name: `R2(vendor, storage, delivery_price)`
   - Reason: FD2 indicates that the attributes \( \text{vendor, storage, delivery\_price} \) are functionally dependent on each other.

#### Checking for Lossless-Join Decomposition:

Now, let's check if the decomposition is lossless-join:

- **Intersection:**
  - Check if the common attribute(s) \( \text{vendor} \) is present in both \( R1 \) and \( R2 \).
  - In this case, there is no common attribute between \( R1 \) and \( R2 \), so the intersection is empty.

- **Union:**
  - Check if the union of the decomposed relations is equal to the original relation \( ebay\_prod\_vendor \).
  - \( R1 \cup R2 = \text{prod\_id, price, model\_num} \cup \text{vendor, storage, delivery\_price} \).
  - The union contains all the attributes from the original relation.

Since the intersection is empty and the union covers all attributes, the decomposition is lossless-join.

#### Decomposed Relations:

- \( R1(\text{prod_id}, \text{price}, \text{model_num}) \)
- \( R2(\text{vendor}, \text{storage}, \text{delivery_price}) \)

This decomposition satisfies the criteria for a lossless-join decomposition. Each relation contains a subset of the original attributes, and their natural join reconstructs the original relation.


## Question #2
To determine if a relation schema \(R\) is in Boyce-Codd Normal Form (BCNF) with respect to a set of functional dependencies \(F\), we need to check if \(R\) satisfies the BCNF conditions. A relation is in BCNF if, for every non-trivial functional dependency \(X \rightarrow Y\) in \(F\), \(X\) is a superkey of \(R\).

Given \(R = (A, B, C, D)\) and \(F = \{CD, C \rightarrow A, B \rightarrow C\}\), let's check if \(R\) is in BCNF:

### Checking BCNF Conditions:

1. **C → A:**
   - \(C\) is not a superkey by itself, but the closure of \(C\) includes \(CD\), which is a superkey. This functional dependency satisfies BCNF.

2. **B → C:**
   - \(B\) is not a superkey by itself, and the closure of \(B\) does not include \(R\) (all attributes). Therefore, this functional dependency violates BCNF.

### Decomposition:

Since \(B \rightarrow C\) violates BCNF, we need to decompose \(R\) into a set of schemas in BCNF. To do this, we will create a new relation for \(B \rightarrow C\) and remove the violating dependency.

1. **Create a Relation for B → C:**
   - Relation Name: \(R_1(B, C)\)
   - Remove \(B \rightarrow C\) from \(F\).

2. **New Relation R' (Without B → C):**
   - \(R' = (A, B, C, D)\) with \(F' = \{CD, C \rightarrow A\}\)

Now, let's check if \(R'\) is in BCNF:

- **C → A:**
   - \(C\) is a superkey by itself, and the closure of \(C\) includes \(CD\), which is a superkey. This functional dependency satisfies BCNF.

### Decomposed Relations:

- \(R_1(B, C)\)
- \(R' = (A, C, D)\) with \(F' = \{CD, C \rightarrow A\}\)

This decomposition satisfies BCNF, as each relation has a superkey as its candidate key, and the original functional dependencies are preserved.

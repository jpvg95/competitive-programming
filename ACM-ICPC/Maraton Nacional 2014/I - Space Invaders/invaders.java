import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

//Available at: https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4654
public class invaders {

	public static void main(String[] args) throws IOException {			
		char shield[][];	
		UnionFind uf;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String line;				
		while((line = br.readLine()) != null){						
			String l[] = line.split(" ");
			int r = Integer.parseInt(l[0]);
			int c = Integer.parseInt(l[1]);
			int s = Integer.parseInt(l[2]);
			
			shield = new char[r][c];
			
			for(int i=0; i<r; i++)
				shield[i] = br.readLine().toCharArray();
			
			boolean breached = false;
			
			if(r==1){
				for(int j=0; j<c; j++){
					if(shield[0][j] == '.'){
						System.out.println(0);
						breached = true;
						for(int scanleft=0; scanleft<s; scanleft++) br.readLine();
						break;
					}
				}				
				if(!breached && s>0){
					int shot = Integer.parseInt(br.readLine());																			
					System.out.println(shot/Math.abs(shot));
					breached = true;
					for(int scanleft=1; scanleft<s; scanleft++) br.readLine();
				}				
			}else{
				uf = new UnionFind(r*c+2);
				
				for(int i=0; i<r; i++){
					for(int j=0; j<c; j++){
						if(shield[i][j] == '.'){
							if(i+1<r && shield[i+1][j] == '.')
								uf.union(index(i,j,c) , index(i+1,j,c));
							if(i-1>=0 && shield[i-1][j] == '.')
								uf.union(index(i,j,c) , index(i-1,j,c));
							if(j+1<c && shield[i][j+1] == '.')
								uf.union(index(i,j,c) , index(i,j+1,c));
							if(j-1>=0 && shield[i][j-1] == '.')
								uf.union(index(i,j,c) , index(i,j-1,c));
						}
					}
				}			
				
				for(int i=0; i<c; i++){
					uf.union(r*c,i);
					uf.union(r*c+1, r*c-i-1);
				}
				
				int firstAlien[], firstNostalgia[];	
				
				if(uf.connected(r*c, r*c+1)){
					System.out.println(0);
					breached = true;
					for(int i=0; i<s; i++) br.readLine();								
				}else{
					firstAlien = new int[c]; 
					Arrays.fill(firstAlien, Integer.MAX_VALUE);
					firstNostalgia = new int[c];
					Arrays.fill(firstNostalgia, -1);
					
					for(int i=0; i<r; i++){
						for(int j=0; j<c; j++){
							if(shield[i][j] == '#'){
								if(i < firstAlien[j])
									firstAlien[j] = i;
								if(i > firstNostalgia[j])
									firstNostalgia[j] = i;
							}						
						}
					}
					
					for(int k=0; k<s; k++){
						int shot = Integer.parseInt(br.readLine());
						int j = Math.abs(shot) - 1;
						int i;
						
						if(shot > 0){
							i = firstAlien[j];
							shield[i][j] = '.';
							for(int fila = firstAlien[j]; fila < r; fila++){
								if(shield[fila][j] == '#'){
									firstAlien[j] = fila; 
									break;
								}
							}
						}else{
							i = firstNostalgia[j];
							shield[i][j] = '.';
							for(int fila = firstNostalgia[j]; fila >=0; fila--){
								if(shield[fila][j] == '#'){
									firstNostalgia[j] = fila; 
									break;
								}
							}
						}
						
						if(i+1<r && shield[i+1][j] == '.')
							uf.union(index(i,j,c) , index(i+1,j,c));
						if(i-1>=0 && shield[i-1][j] == '.')
							uf.union(index(i,j,c) , index(i-1,j,c));
						if(j+1<c && shield[i][j+1] == '.')
							uf.union(index(i,j,c) , index(i,j+1,c));
						if(j-1>=0 && shield[i][j-1] == '.')
							uf.union(index(i,j,c) , index(i,j-1,c));
						
						if(uf.connected(r*c, r*c+1)){
							System.out.println((k+1)*shot/Math.abs(shot));
							breached = true;
							for(int scanleft=k+1; scanleft<s; scanleft++) br.readLine();
							break;
						}
					}
				}
			}			
			
			if(!breached)
				System.out.println("X");			
		}
		br.close();				
	}
	
	public static int index(int i, int j, int c){
		return j+i*c;
	}	
}

class UnionFind{
	private int parent[];
	private int size[];
	//private int components;
	
	public UnionFind(int n){
		//components = n;
		parent = new int[n];
		size = new int[n];
		for(int i=0; i<n; i++){
			parent[i] = i;
			size[i] = 1;
		}
	}
	
	private int root(int p){
		while(p != parent[p]){
			parent[p] = parent[parent[p]];
			p = parent[p];
		}			
		return p;
	}
	
	public void union(int p, int q){
		int rootP = root(p);
		int rootQ = root(q);
		
		if(rootP != rootQ){
			if(size[rootP] < size[rootQ]){
				parent[rootP] = rootQ;
				size[rootQ] = size[rootQ] + size[rootP];				
			}else{
				parent[rootQ] = rootP;
				size[rootP] = size[rootP] + size[rootQ];		
			}
			//components--;
		}
	}
	
	public boolean connected(int p, int q){
		return root(p) == root(q);
	}
}
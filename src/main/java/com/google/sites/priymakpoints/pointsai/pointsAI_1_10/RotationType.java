package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

public enum RotationType implements Variables{

	r0,r90,r180,r270,GORIZONTAL,VERTICAL,GORIZONTAL90,VERTICAL90,ERROR;

	public static String getTransform(RotationType type,String str) {
		String str1="";
		boolean isSquare=isSquareTemplate(str);
		switch (type) {
		case r0:return str;
		case r90:
			if(!isSquare)return str;
			str=str.replaceAll("O", "");
			for(int i=0;i<9;i++){for(int j=9-1;j>=0;j--)str1+=str.substring(9*j+i,9*j+i+1);str1+="OOOO";}
			return str1;
		case r180:
			if(isSquare){str=getTransform(RotationType.r90,str);return(getTransform(RotationType.r90,str));}
			else{for(int i=sizeY_TE-1;i>=0;i--)for(int j=sizeX_TE-1;j>=0;j--)str1+=str.substring(sizeX_TE*i+j,i*sizeX_TE+j+1);return str1;}
		case r270:if(!isSquare)return str;str=getTransform(RotationType.r180,str);return(getTransform(RotationType.r90,str));
		case GORIZONTAL:
			if(isSquare){str=getTransform(RotationType.r180,str);return(getTransform(RotationType.VERTICAL,str));}
			else{for(int j=0;j<sizeY_TE;j++)for(int i=0;i<sizeX_TE;i++)str1+=str.substring((j+1)*sizeX_TE-i-1,(j+1)*sizeX_TE-i);return str1;}
		case VERTICAL:for(int i=sizeY_TE-1;i>=0;i--)str1+=str.substring(sizeX_TE*i,(i+1)*sizeX_TE);return str1;
		case GORIZONTAL90:if(!isSquare)return str;str=getTransform(RotationType.GORIZONTAL,str);return(getTransform(RotationType.r90,str));
		case VERTICAL90:if(!isSquare)return str;str=getTransform(RotationType.VERTICAL,str);return(getTransform(RotationType.r90,str));
		case ERROR:return str;
		default:return str;
		}
	}

	public static boolean isSquareTemplate(String template){
		int squareEfficient=0;
		for(int i=0;i<sizeY_TE;i++){
			if(template.substring(sizeX_TE*i+9,sizeX_TE*i+10).equals("O"))squareEfficient++;
			if(template.substring(sizeX_TE*i+10,sizeX_TE*i+11).equals("O"))squareEfficient++;
			if(template.substring(sizeX_TE*i+11,sizeX_TE*i+12).equals("O"))squareEfficient++;
			if(template.substring(sizeX_TE*i+12,sizeX_TE*i+13).equals("O"))squareEfficient++;
		}
		return squareEfficient == 36;
	}

}

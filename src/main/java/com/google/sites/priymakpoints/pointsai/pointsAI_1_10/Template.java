package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

import java.awt.*;

public class Template implements Variables {

	private int index = 0;
	private TemplateType type = null;
	private final String template;
	private String template90;
	private final String template180;
	private String template270;
	private final String templateVert;
	private final String templateGor;
	private String templateVert90;
	private String templateGor90;
	private final String templateWithoutTargets;
	private String template90WithoutTargets;
	private final String template180WithoutTargets;
	private String template270WithoutTargets;
	private final String templateVertWithoutTargets;
	private final String templateGorWithoutTargets;
	private String templateVert90WithoutTargets;
	private String templateGor90WithoutTargets;
	private boolean isSide = false;
	public String targetRotateTemplate;
	public RotationType targetRotateType;

	public Template(String strTemplate) {

		index = new Integer(strTemplate.substring(117, 122));
		type = TemplateType.getTemplateType(strTemplate.substring(122, 125));
		boolean isSquare = type.isSquare();
		isSide = type.isSide();

		template = strTemplate.substring(0, 117);
		template180 = RotationType.getTransform(RotationType.r180, template);
		templateGor = RotationType.getTransform(RotationType.GORIZONTAL, template);
		templateVert = RotationType.getTransform(RotationType.VERTICAL, template);

		if (isSquare) {
			template90 = RotationType.getTransform(RotationType.r90, template);
			template270 = RotationType.getTransform(RotationType.r270, template);
			templateGor90 = RotationType.getTransform(RotationType.GORIZONTAL90, template);
			templateVert90 = RotationType.getTransform(RotationType.VERTICAL90, template);
		}

		templateWithoutTargets = getTemplateWithoutTargets(template);
		template180WithoutTargets = getTemplateWithoutTargets(template180);
		templateVertWithoutTargets = getTemplateWithoutTargets(templateVert);
		templateGorWithoutTargets = getTemplateWithoutTargets(templateGor);

		if (isSquare) {
			template90WithoutTargets = getTemplateWithoutTargets(template90);
			template270WithoutTargets = getTemplateWithoutTargets(template270);
			templateVert90WithoutTargets = getTemplateWithoutTargets(templateVert90);
			templateGor90WithoutTargets = getTemplateWithoutTargets(templateGor90);
		}

	}

	public TemplateType getTemplateType() {
		return type;
	}

	public int getTemplateIndex() {
		return index;
	}

	public String toString() {
		return template + index + type;
	}

	public String getTemplate() {
		return template;
	}

	public Point getMoveAI() {
		return getPointCoordinates(targetRotateTemplate, DotType.RED_NORMAL);
	}

	public boolean isEqualsLikeArea(String str, TemplateType type) {
		if (this.type != type) return false;
		try {
			str = getTemplateWithoutTargetsExceptE(str);
		} catch (Exception e) {
			System.out.println("occurred error string result: " + str);
			System.out.println("occurred error on template type: " + type.toString());
			e.printStackTrace();
		}

		if (!isSide) {
			if (isEquals(str, templateWithoutTargets)) {
				targetRotateTemplate = template;
				targetRotateType = RotationType.r0;
				return true;
			}
			if (isEquals(str, template180WithoutTargets)) {
				targetRotateTemplate = template180;
				targetRotateType = RotationType.r180;
				return true;
			}
			if (isEquals(str, templateGorWithoutTargets)) {
				targetRotateTemplate = templateGor;
				targetRotateType = RotationType.GORIZONTAL;
				return true;
			}
			if (isEquals(str, templateVertWithoutTargets)) {
				targetRotateTemplate = templateVert;
				targetRotateType = RotationType.VERTICAL;
				return true;
			}
			if (isEquals(str, template90WithoutTargets)) {
				targetRotateTemplate = template90;
				targetRotateType = RotationType.r90;
				return true;
			}
			if (isEquals(str, template270WithoutTargets)) {
				targetRotateTemplate = template270;
				targetRotateType = RotationType.r270;
				return true;
			}
			if (isEquals(str, templateGor90WithoutTargets)) {
				targetRotateTemplate = templateGor90;
				targetRotateType = RotationType.GORIZONTAL90;
				return true;
			}
			if (isEquals(str, templateVert90WithoutTargets)) {
				targetRotateTemplate = templateVert90;
				targetRotateType = RotationType.VERTICAL90;
				return true;
			}
		}

		if (isSide) {
			if (isEquals(str, getTemplateWithoutTargetsExceptE(template))) {
				targetRotateTemplate = template;
				targetRotateType = RotationType.r0;
				return true;
			}
			if (isEquals(str, getTemplateWithoutTargetsExceptE(template180))) {
				targetRotateTemplate = template180;
				targetRotateType = RotationType.r180;
				return true;
			}
			if (isEquals(str, getTemplateWithoutTargetsExceptE(templateGor))) {
				targetRotateTemplate = templateGor;
				targetRotateType = RotationType.GORIZONTAL;
				return true;
			}
			if (isEquals(str, getTemplateWithoutTargetsExceptE(templateVert))) {
				targetRotateTemplate = templateVert;
				targetRotateType = RotationType.VERTICAL;
				return true;
			}
			if (isEquals(str, getTemplateWithoutTargetsExceptE(template90))) {
				targetRotateTemplate = template90;
				targetRotateType = RotationType.r90;
				return true;
			}
			if (isEquals(str, getTemplateWithoutTargetsExceptE(template270))) {
				targetRotateTemplate = template270;
				targetRotateType = RotationType.r270;
				return true;
			}
			if (isEquals(str, getTemplateWithoutTargetsExceptE(templateGor90))) {
				targetRotateTemplate = templateGor90;
				targetRotateType = RotationType.GORIZONTAL90;
				return true;
			}
			if (isEquals(str, getTemplateWithoutTargetsExceptE(templateVert90))) {
				targetRotateTemplate = templateVert90;
				targetRotateType = RotationType.VERTICAL90;
				return true;
			}
		}

		return false;
	}

	boolean isEquals(String str, String template) {
		try {
			int similarity = 0;
			for (int i = 0; i < template.length(); i++) {
				if (str.substring(i, i + 1).equals(DotType.RED.toString())) {
					if (template.substring(i, i + 1).equals(DotType.ANY.toString()) | template.substring(i, i + 1).equals(DotType.RED_EMPTY.toString()) |
						template.substring(i, i + 1).equals(DotType.RED.toString())) {
						similarity++;
					} else return false;
				}
				if (str.substring(i, i + 1).equals(DotType.BLUE.toString())) {
					if (template.substring(i, i + 1).equals(DotType.ANY.toString()) | template.substring(i, i + 1).equals(DotType.BLUE.toString()) |
						template.substring(i, i + 1).equals(DotType.BLUE_EMPTY.toString()) |
						(template.substring(i, i + 1).equals(DotType.BLUE_TARGET.toString()) & !isSide)
						) {
						similarity++;
					} else return false;
				}
				if (str.substring(i, i + 1).equals(DotType.BLUE_TARGET.toString())) {
					if (template.substring(i, i + 1).equals(DotType.BLUE_TARGET.toString()) & isSide) {
						similarity++;
					} else return false;
				}
				if (str.substring(i, i + 1).equals(DotType.LAND.toString())) {
					if (template.substring(i, i + 1).equals(DotType.LAND.toString())) {
						similarity++;
					} else return false;
				}
				if (str.substring(i, i + 1).equals(DotType.OUT.toString())) {
					if (template.substring(i, i + 1).equals(DotType.OUT.toString())) {
						similarity++;
					} else return false;
				}
				if (str.substring(i, i + 1).equals(DotType.NULL.toString())) {
					if (template.substring(i, i + 1).equals(DotType.NULL.toString()) | template.substring(i, i + 1).equals(DotType.ANY.toString()) |
						template.substring(i, i + 1).equals(DotType.RED_EMPTY.toString()) | template.substring(i, i + 1).equals(DotType.BLUE_EMPTY.toString())) {
						similarity++;
					} else return false;
				}
				if (str.substring(i, i + 1).equals(DotType.GLOBAL.toString())) {
					if (template.substring(i, i + 1).equals(DotType.GLOBAL.toString()) |
						template.substring(i, i + 1).equals(DotType.ANY.toString())) {
						similarity++;
					} else return false;
				}
			}
			return similarity == 117;
		} catch (Exception e) {
			return false;
		}
	}

	public Point getPointCoordinates(String content, DotType dot) {
		for (int i = 0; i < content.length(); i++)
			if (content.substring(i, i + 1).equals(dot.toString())) {
				return new Point(i % sizeX_TE, i / sizeX_TE);
			}
		return null;
	}

	private String getTemplateWithoutTargetsExceptE(String str) {
		str = str.replaceAll(DotType.RED_NORMAL.toString(), DotType.NULL.toString());
		str = str.replaceAll(DotType.RED_ATTACK.toString(), DotType.NULL.toString());
		str = str.replaceAll(DotType.RED_CAPTURE.toString(), DotType.NULL.toString());
		str = str.replaceAll(DotType.RED_DEFENCE.toString(), DotType.NULL.toString());
		str = str.replaceAll(DotType.RED_GROUND.toString(), DotType.NULL.toString());
		str = str.replaceAll(DotType.RED_PROTECTION.toString(), DotType.NULL.toString());
		if (!isSide)
			str = str.replaceAll(DotType.BLUE_TARGET.toString(), DotType.BLUE.toString());
		str = str.replaceAll(DotType.BLUE_NORMAL.toString(), DotType.NULL.toString());
		return str;
	}

	private String getTemplateWithoutTargets(String str) {
		str = str.replaceAll(DotType.RED_NORMAL.toString(), DotType.NULL.toString());
		str = str.replaceAll(DotType.RED_ATTACK.toString(), DotType.NULL.toString());
		str = str.replaceAll(DotType.RED_CAPTURE.toString(), DotType.NULL.toString());
		str = str.replaceAll(DotType.RED_DEFENCE.toString(), DotType.NULL.toString());
		str = str.replaceAll(DotType.RED_GROUND.toString(), DotType.NULL.toString());
		str = str.replaceAll(DotType.RED_PROTECTION.toString(), DotType.NULL.toString());
		str = str.replaceAll(DotType.BLUE_TARGET.toString(), DotType.BLUE.toString());
		str = str.replaceAll(DotType.BLUE_NORMAL.toString(), DotType.NULL.toString());
		return str;
	}

}

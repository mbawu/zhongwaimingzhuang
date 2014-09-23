package com.bdh.model;

import com.bdh.model.NetworkAction;

;
public class ErrorMsg {

	/**
	 * ������������ݲ������ͻ�ȡ��Ӧ�Ĵ�����Ϣ
	 * 
	 * @param request
	 *            ����������͵����ݴ�������
	 * @param code
	 *            ���صĴ������
	 * @return
	 */
	public static String getErrorMsg(NetworkAction request, int code) {
		String result = null;
		if (request.equals(NetworkAction.��֤��֤��)) {
			switch (code) {
			case 0:
				result = "�ύ��Ϣ��ȫ";
				break;

			case 1:
				result = "��֤�ɹ�";
				break;
			case 2:
				result = "��֤�����";
				break;
			default:
				break;
			}
		} else if (request.equals(NetworkAction.�û�ע��)) {
			switch (code) {
			case 2:
				result = "�û���Ϊ20���ڣ�����Ϊ��";
				break;

			case 3:
				result = "����Ϊ�� �� ���Ȳ���6��32λ�ַ�֮��";
				break;
			case 4:
				result = "�����������벻һ��";
				break;
			case 5:
				result = "����Ϊ�� �� ��ʽ���Ϸ�";
				break;
			case 6:
				result = "�û����Ѿ�����";
				break;
			case 7:
				result = "ʧ��";
				break;
			case 8:
				result = "����ID����";
				break;
			case 9:
				result = "���ֻ������ѱ�ռ��";
				break;
			case 10:
				result = "������֤ʧ��";
				break;
			default:
				break;
			}
		} else if (request.equals(NetworkAction.�ҵ���Ϣ)) {
			switch (code) {
			case 3:
				result = "δ��¼";
				break;
			case 6:
				result = "���ݿ����";
				break;
			}
		} else if (request.equals(NetworkAction.��¼)) {
			switch (code) {
			case 2:
				result = "�û���Ϊ��";
				break;
			case 3:
				result = "����Ϊ��";
				break;
			case 4:
				result = "�û����������򲻴���";
				break;
			case 5:
				result = "�������";
				break;
			case 6:
				result = "���ݿ����";
				break;
			}
		} else if (request.equals(NetworkAction.�һ�����)) {
			switch (code) {
			case 2:
				result = "����ʧ��";
				break;
			case 3:
				result = "�û������ֻ����벻ƥ��";
				break;
			}
		}
		else if (request.equals(NetworkAction.�޸�����)) {
			switch (code) {
			case 2:
				result = "ԭ�������";
				break;
			case 3:
				result = "δ��¼";
				break;
			case 4:
				result = "ϵͳ��æ�����Ժ�����";
				break;
			}
		}
		else if (request.equals(NetworkAction.�����ջ���ַ)) {
			switch (code) {
			case 2:
				result = "δ��¼";
				break;
			case 3:
				result = "�ύ��������";
				break;
			case 4:
				result = "��ഴ��10����ַ";
				break;
			}
		}
		else if (request.equals(NetworkAction.��ȡ�ջ���ַ�б�)) {
			switch (code) {
			case 2:
				result = "δ��¼";
				break;
			}
		}
		else if (request.equals(NetworkAction.����Ĭ�ϵ�ַ)) {
			switch (code) {
			case 2:
				result = "δ��¼";
				break;
			}
		}
		else if (request.equals(NetworkAction.��ѯ������Ʒ���Ż�ȯ)) {
			switch (code) {
			case 3:
				result = "δ��¼";
				break;
			case 6:
				result = "���ݿ����";
				break;
			}
		}
		else if (request.equals(NetworkAction.�ҵ��Ż�ȯ)) {
			switch (code) {
			case 0:
				result = "����ʧ��";
				break;
			case 3:
				result = "δ��¼";
				break;
			case 6:
				result = "���ݿ����";
				break;
			}
		}
		else if (request.equals(NetworkAction.���������Ϣ)) {
			switch (code) {
			case 0:
				result = "����ʧ��";
				break;
			case 3:
				result = "δ��¼";
				break;
			case 2:
				result = "���ݿ����";
				break;
			}
		}
		else if (request.equals(NetworkAction.��������)) {
			switch (code) {
			case 2:
				result = "δ��¼";
				break;
			case 3:
				result = "��������";
				break;
			case 6:
				result = "���ݿ����";
				break;
			}
		}
		else if (request.equals(NetworkAction.ȡ������)) {
			switch (code) {
			case 0:
				result = "����ʧ��";
				break;
			case 2:
				result = "�������޸ĸö��������Լ��Ķ����򶩵������ڣ�";
				break;
			case 3:
				result = "δ��¼";
				break;
			}
		}
		else if (request.equals(NetworkAction.ȷ���ջ�)) {
			switch (code) {
			case 0:
				result = "����ʧ��";
				break;
			case 2:
				result = "��������";
				break;
			case 3:
				result = "δ��¼";
				break;
			case 6:
				result = "���ݿ����";
				break;
			}
		}
		else if (request.equals(NetworkAction.���۶���)) {
			switch (code) {
			case 0:
				result = "����ʧ��";
				break;
			case 4:
				result = "�����������۸���Ʒ(û�й����û����ɽ���)";
				break;
			case 3:
				result = "δ��¼";
				break;
			case 6:
				result = "���ݿ����";
				break;
			}
		}
		else if (request.equals(NetworkAction.�������)) {
			switch (code) {
			case 0:
				result = "����ʧ��";
				break;
			case 2:
				result = "ʮ������ֻ������һ��";
				break;
			case 3:
				result = "δ��¼";
				break;
			}
		}
		else {
			result = "����ʧ��";
		}
		return result;
	}
}

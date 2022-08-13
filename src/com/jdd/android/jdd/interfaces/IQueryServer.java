package com.jdd.android.jdd.interfaces;

import android.os.Bundle;

/**
 * ��������ѯ���ݽӿ�
 *
 * @author ������
 * @version 1.0
 * @corporation ��µ�
 * @date 2015-06-24
 * @since 1.0
 */
public interface IQueryServer {
    /**
     * ��ѯ�ӿڳɹ�
     * @param taskId    ����ID����������ӿ�
     * @param bundle    ��������������
     */
    public void onQuerySuccess(int taskId, Bundle bundle);

    /**
     * ��ѯ�ӿ�ʧ��
     * @param taskId    ����ID����������ӿ�
     * @param bundle    ��������������
     */
    public void onServerError(int taskId, Bundle bundle);

    /**
     * �����쳣
     * @param taskId    ����ID����������ӿ�
     * @param bundle    ��������������
     */
    public void onNetError(int taskId, Bundle bundle);

    /**
     * �ڲ��쳣
     * @param taskId    ����ID����������ӿ�
     * @param bundle    ��������������
     */
    public void onInternalError(int taskId, Bundle bundle);
}

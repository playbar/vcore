//
// Created by mac on 16/8/17.
//

#ifndef ASGLES3JNI_THREADPOOL_H
#define ASGLES3JNI_THREADPOOL_H


#include <string.h>
#include <pthread.h>

using namespace std;

/**
 * 执行任务的类，设置任务数据并执行
 */
class CTask
{
protected:
    char* m_strTaskName = NULL;  /** 任务的名称 */
    void* m_ptrData;       /** 要执行的任务的具体数据 */
public:
    CTask(){prev = NULL; next = NULL;}
    CTask(char* taskName)
    {
        prev = NULL; next = NULL;
        int len = strlen(taskName);
        if( len > 0) {
            m_strTaskName = (char*)malloc(len+1);
            memcpy(m_strTaskName, taskName, len);
            m_strTaskName[len] = 0;
        }
        m_ptrData = NULL;
    }
    virtual int Run()= 0;
    void SetData(void* data);    /** 设置任务数据 */

public:
    virtual ~CTask(){
        if(NULL != m_strTaskName) {
            free(m_strTaskName);
        }
    }

public:
    CTask *prev;
    CTask *next;
};

/**
 * 线程池管理类的实现
 */
class CThreadPool
{
private:
    static  CTask* m_vecTaskList;     /** 任务列表 */
    static  bool shutdown;                    /** 线程退出标志 */
    int     m_iThreadNum;                     /** 线程池中启动的线程数 */
    pthread_t   *pthread_id;
    bool mbCreate;

    static pthread_mutex_t m_pthreadMutex;    /** 线程同步锁 */
    static pthread_cond_t m_pthreadCond;      /** 线程同步的条件变量 */

protected:
    static void* ThreadFunc(void * threadData); /** 新线程的线程回调函数 */
    static bool mbNeedMakeCurrent;
    static int MoveToIdle(pthread_t tid);       /** 线程执行结束后，把自己放入到空闲线程中 */
    static int MoveToBusy(pthread_t tid);       /** 移入到忙碌线程中去 */



public:
    CThreadPool(int threadNum = 10);
    ~CThreadPool();
    int Create();          /** 创建线程池中的线程 */
    int AddTask(CTask *task);      /** 把任务添加到任务队列中 */
    int StopAll();                 /** 使线程池中的线程退出 */
    int getTaskSize();             /** 获取当前任务队列中的任务数 */
};


#endif //ASGLES3JNI_THREADPOOL_H

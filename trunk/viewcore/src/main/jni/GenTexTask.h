//
// Created by mac on 16/8/19.
//

#ifndef TRUNK_GENTEXTASK_H
#define TRUNK_GENTEXTASK_H

#include "ThreadPool.h"
#include "GLThreadUtilJni.h"
#include "viewcore.h"

class GenTexTask: public CTask
{
public:
    GenTexTask();
    int Run();

public:
    GLuint CreateSimpleTexture2D( );
};


#endif //TRUNK_GENTEXTASK_H
